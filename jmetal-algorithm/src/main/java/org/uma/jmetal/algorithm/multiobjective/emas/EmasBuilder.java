package org.uma.jmetal.algorithm.multiobjective.emas;

/**
 * Created by adamzima on 24/04/16.
 */

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import java.util.List;

public class EmasBuilder<S extends Solution<?>> implements AlgorithmBuilder<Emas<S>> {
  /**
   * EmasBuilder class
   */
  protected final Problem<S> problem;
  private final DominanceComparator<S> dominanceComparator;
  protected int maxIterations;
  protected int populationSize;
  protected CrossoverOperator<S> crossoverOperator;
  protected MutationOperator<S> mutationOperator;
  protected SelectionOperator<List<S>, S> selectionOperator;
  protected SolutionListEvaluator<S> evaluator;
  private int deathThreshold;
  private int reproductionThreshold;
  private int meetingCost;
  private int startingEnergy;
  private int reproductionCost;
  private double neighbourhoodThreshold;

  /**
   * EmasBuilder constructor
   */
  public EmasBuilder(Problem<S> problem, CrossoverOperator<S> crossoverOperator,
                     MutationOperator<S> mutationOperator, DominanceComparator<S> dominanceComparator) {
    this.problem = problem;
    this.maxIterations = 250;
    this.populationSize = 100;
    this.crossoverOperator = crossoverOperator ;
    this.mutationOperator = mutationOperator ;
    this.dominanceComparator = dominanceComparator;
    this.neighbourhoodThreshold = 0;
  }

  public EmasBuilder<S> setMaxIterations(int maxIterations) {
    if (maxIterations < 0) {
      throw new JMetalException("maxIterations is negative: " + maxIterations);
    }
    this.maxIterations = maxIterations;

    return this;
  }

  public EmasBuilder<S> setPopulationSize(int populationSize) {
    if (populationSize < 0) {
      throw new JMetalException("Population size is negative: " + populationSize);
    }

    this.populationSize = populationSize;

    return this;
  }

  public EmasBuilder<S> setStartingEnergy(int energy) {
    this.startingEnergy = energy;

    return this;
  }

  public EmasBuilder<S> setDeathThreshold(int threshold) {
    this.deathThreshold = threshold;

    return this;
  }

  public EmasBuilder<S> setReproductionThreshold(int threshold) {
    this.reproductionThreshold = threshold;

    return this;
  }

  public EmasBuilder<S> setMeetingCost(int energy) {
    this.meetingCost = energy;

    return this;
  }

  public Emas<S> build() {
    return new Emas<S>(problem, maxIterations, populationSize, startingEnergy, deathThreshold,
        reproductionThreshold, meetingCost, reproductionCost, crossoverOperator,
        mutationOperator, dominanceComparator, neighbourhoodThreshold);
  }

  public Problem<S> getProblem() {
    return problem;
  }

  public int getMaxIterations() {
    return maxIterations;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public CrossoverOperator<S> getCrossoverOperator() {
    return crossoverOperator;
  }

  public MutationOperator<S> getMutationOperator() {
    return mutationOperator;
  }

  public EmasBuilder setReproductionCost(int energy) {
    this.reproductionCost = energy;

    return this;
  }

  public EmasBuilder setNeighbourhoodThreshold(double neighbourhoodThreshold) {
    this.neighbourhoodThreshold = neighbourhoodThreshold;

    return this;
  }
}
