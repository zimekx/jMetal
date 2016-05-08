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
  protected void die(List<AbstractEmasAgent<S>> nextPopulation) {
    System.out.println("Death!");

    System.out.println("\t"+this.solution.getObjective(0));
    System.out.println("\t"+this.solution.getObjective(1));

    S solution = (S) problem.createSolution();
    problem.evaluate(solution);

    this.solution = solution;

    nextPopulation.add(this);
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

    System.out.println("\t"+betterAgent.solution.getObjective(0));
    System.out.println("\t"+worseAgent.solution.getObjective(0));
    System.out.println("\t"+betterAgent.solution.getObjective(1));
    System.out.println("\t"+worseAgent.solution.getObjective(1));


    betterAgent.transferEnergy(meetingCost);
    worseAgent.transferEnergy(-meetingCost);
  }

  @Override
  protected void reproduce(List<AbstractEmasAgent<S>> population, List<AbstractEmasAgent<S>> nextPopulation) {
    List<AbstractEmasAgent<S>> possibleParents = population.stream().filter(
        a -> (a.getEnergyLevel() > reproductionThreshold && a != this)
    ).collect(Collectors.toList());
    if (possibleParents.size() < 1) {
      nextPopulation.add(this);
      return;
    }

    System.out.println("Reproduction!");

    EmasAgent<S> otherParent = getRandomAgent(possibleParents);

    List<S> parents = new ArrayList<>(2);
    parents.add(this.getSolution());
    parents.add(otherParent.getSolution());

    List<S> offspring = crossoverOperator.execute(parents);
    mutationOperator.execute(offspring.get(0));
    mutationOperator.execute(offspring.get(1));
    problem.evaluate(offspring.get(0));
    problem.evaluate(offspring.get(1));

    EmasAgent<S> firstChild = new EmasAgent<>(offspring.get(0), problem, startingEnergy,
        deathThreshold, reproductionThreshold, meetingCost, reproductionCost,
        crossoverOperator, mutationOperator, dominanceComparator);
    EmasAgent<S> secondChild = new EmasAgent<>(offspring.get(1), problem, startingEnergy,
        deathThreshold, reproductionThreshold, meetingCost, reproductionCost,
        crossoverOperator, mutationOperator, dominanceComparator);

    this.transferEnergy(-reproductionCost);
    otherParent.transferEnergy(-reproductionCost);

    List<AbstractEmasAgent<S>> competitors = new ArrayList<>(4);
    competitors.add(this);
    competitors.add(otherParent);
    competitors.add(firstChild);
    competitors.add(secondChild);

    nextPopulation.addAll(getTwoBestAgents(competitors));
  }

  private EmasAgent<S> getRandomAgent(List<AbstractEmasAgent<S>> population) {
    EmasAgent<S> agent;
    do {
      agent = (EmasAgent<S>) population.get(JMetalRandom.getInstance().nextInt(0, population.size()-1));
    } while (agent == this);
    return agent;
  }

  private List<AbstractEmasAgent<S>> getTwoBestAgents(List<AbstractEmasAgent<S>> competitors) {
    Map<AbstractEmasAgent<S>, Integer> wins = new HashMap<>();
    wins.put(competitors.get(0), 0);
    wins.put(competitors.get(1), 0);
    wins.put(competitors.get(2), 0);
    wins.put(competitors.get(3), 0);

    for (int i = 0; i < 3; i++) {
      for (int j = i + 1; j < 4; j++) {
        AbstractEmasAgent<S> agent1 = (AbstractEmasAgent<S>) wins.keySet().toArray()[i];
        AbstractEmasAgent<S> agent2 = (AbstractEmasAgent<S>) wins.keySet().toArray()[j];

        int result = dominanceComparator.compare(agent1.getSolution(), agent2.getSolution());
        if (result == 0) {
          wins.put(agent1, wins.get(agent1) + 1);
          wins.put(agent2, wins.get(agent2) + 1);
        } else if (result == -1) {
          wins.put(agent1, wins.get(agent1) + 1);
        } else {
          wins.put(agent2, wins.get(agent2) + 1);
        }
      }
    }

    List<AbstractEmasAgent<S>> sorted = new LinkedList<>();
    wins.entrySet().
        stream().
        sorted(Map.Entry.comparingByValue()).
        forEachOrdered(e -> sorted.add(e.getKey()));
    return sorted.subList(0, 2);
  }
}
