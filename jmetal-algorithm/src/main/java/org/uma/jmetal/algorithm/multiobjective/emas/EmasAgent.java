package org.uma.jmetal.algorithm.multiobjective.emas;

import org.uma.jmetal.algorithm.impl.AbstractEmasAgent;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by adamzima on 24/04/16.
 */
public class EmasAgent<S extends Solution<?>> extends AbstractEmasAgent<S> {

  private final Problem problem;
  private final int startingEnergy;

  private final CrossoverOperator<S> crossoverOperator;
  private final MutationOperator<S> mutationOperator;
  private final Comparator<S> dominanceComparator;

  protected EmasAgent(S solution, Problem problem, int startingEnergy,
                      int deathThreshold, int reproductionThreshold, int meetingCost, int reproductionCost,
                      CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
                      Comparator<S> dominanceComparator) {
    super(solution, startingEnergy, deathThreshold, reproductionThreshold, meetingCost, reproductionCost);
    this.problem = problem;
    this.startingEnergy = startingEnergy;
    this.crossoverOperator = crossoverOperator;
    this.mutationOperator = mutationOperator;
    this.dominanceComparator = dominanceComparator;
  }

  public S getSolution() {
    return solution;
  }

  @Override
  protected void die() {
    System.out.println("Death!");
  }

  public void transferEnergy(int amount) {
    energyLevel += amount;
  }

  protected void meet(List<AbstractEmasAgent<S>> population) {
    EmasAgent<S> otherAgent = getRandomAgent(population);
    int compareResult = dominanceComparator.compare(this.solution, otherAgent.getSolution());

    EmasAgent<S> betterAgent;
    EmasAgent<S> worseAgent;
    if (compareResult == 0) {
      return;
    }

    System.out.println("Meeting!");
    if (compareResult < 0) {
      betterAgent = this;
      worseAgent = otherAgent;
    } else {
      betterAgent = otherAgent;
      worseAgent = this;
    }

    betterAgent.transferEnergy(meetingCost);
    worseAgent.transferEnergy(-meetingCost);
  }

  @Override
  protected void reproduce(List<AbstractEmasAgent<S>> population, List<AbstractEmasAgent<S>> nextPopulation) {
    List<AbstractEmasAgent<S>> possibleParents = population.stream().filter(
        a -> (a.getEnergyLevel() > reproductionThreshold && a != this)
    ).collect(Collectors.toList());
    if (possibleParents.size() < 1)
      return;

    System.out.println("Reproduction!");

    EmasAgent<S> otherParent = getRandomAgent(possibleParents);

    List<S> parents = new ArrayList<>(2);
    parents.add(this.getSolution());
    parents.add(otherParent.getSolution());

    List<S> offspring = crossoverOperator.execute(parents);
    mutationOperator.execute(offspring.get(0));
    mutationOperator.execute(offspring.get(1));

    EmasAgent<S> firstChild = new EmasAgent<>(offspring.get(0), problem, startingEnergy,
        deathThreshold, reproductionThreshold, meetingCost, reproductionCost,
        crossoverOperator, mutationOperator, dominanceComparator);
    EmasAgent<S> secondChild = new EmasAgent<>(offspring.get(1), problem, startingEnergy,
        deathThreshold, reproductionThreshold, meetingCost, reproductionCost,
        crossoverOperator, mutationOperator, dominanceComparator);

    this.transferEnergy(-reproductionCost);
    otherParent.transferEnergy(-reproductionCost);

    nextPopulation.add(this);
    nextPopulation.add(otherParent);
    nextPopulation.add(firstChild);
    nextPopulation.add(secondChild);
  }

  private EmasAgent<S> getRandomAgent(List<AbstractEmasAgent<S>> population) {
    EmasAgent<S> agent;
    do {
      agent = (EmasAgent<S>) population.get(JMetalRandom.getInstance().nextInt(0, population.size()-1));
    } while (agent == this);
    return agent;
  }
}
