package org.uma.jmetal.algorithm.multiobjective.emas;

import org.uma.jmetal.algorithm.impl.AbstractEmasAlgorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by adamzima on 24/04/16.
 */
public class Emas<S extends Solution<?>> extends AbstractEmasAlgorithm<S, List<S>, EmasAgent<S>> {
  protected final Problem<S> problem;
  protected final int maxIterations;
  protected final int populationSize;

  protected final int startingEnergy;
  protected final int deathThreshold;
  protected final int reproductionThreshold;
  protected final int meetingCost;
  protected final int reproductionCost;

  private final CrossoverOperator<S> crossoverOperator;
  private final MutationOperator<S> mutationOperator;
  private final Comparator<S> dominanceComparator;
  private final String experimentDirectoryPath;
  private final double neighbourhoodThreshold;

  private int iterations;

  public Emas(Problem<S> problem, int maxIterations, int populationSize, int startingEnergy,
              int deathThreshold, int reproductionThreshold, int meetingCost, int reproductionCost,
              CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
              Comparator<S> dominanceComparator, double neighbourhoodThreshold) {
    this.problem = problem;
    this.maxIterations = maxIterations;
    this.populationSize = populationSize;

    this.startingEnergy = startingEnergy;
    this.deathThreshold = deathThreshold;
    this.reproductionThreshold = reproductionThreshold;
    this.meetingCost = meetingCost;
    this.reproductionCost = reproductionCost;

    this.crossoverOperator = crossoverOperator;
    this.mutationOperator = mutationOperator;
    this.dominanceComparator = dominanceComparator;

    this.neighbourhoodThreshold = neighbourhoodThreshold;

    this.experimentDirectoryPath = "/Users/adamzima/semestr8/jmetal-5/jMetal/experiment/ZDTStudy/data/" + getName() + "/" + problem.getName() + "/";
  }

  @Override
  public List<S> getResult() {
//    return SolutionListUtils.getNondominatedSolutions(getCurrentObjectives());
    List<S> currentObjectives = getCurrentObjectives();
    return currentObjectives;
  }

  @Override
  protected void initProgress() {
    iterations = 1;
  }

  @Override
  protected void updateProgress() {
    new SolutionListOutput((List<? extends S>) getResult())
        .setSeparator("\t")
        .setVarFileOutputContext(new DefaultFileOutputContext(experimentDirectoryPath + "VAR" + iterations + ".tsv"))
        .setFunFileOutputContext(new DefaultFileOutputContext(experimentDirectoryPath + "FUN" + iterations + ".tsv"))
        .print();

    iterations++;
  }

  @Override
  protected boolean isStoppingConditionReached() {
    return iterations >= maxIterations;
  }

  @Override
  protected List<EmasAgent<S>> createInitialPopulation() {
    List<EmasAgent<S>> population = new CopyOnWriteArrayList<>();

    for (int i = 0; i < populationSize - 1; i++) {
      S solution = problem.createSolution();
      problem.evaluate(solution);

      EmasAgent<S> newAgent = new EmasAgent<>(solution, problem,
          startingEnergy, deathThreshold, reproductionThreshold, meetingCost, reproductionCost,
          crossoverOperator, mutationOperator, dominanceComparator, neighbourhoodThreshold);
      population.add(newAgent);
    }

    return population;
  }

  private List<S> getCurrentObjectives() {
    return getPopulation().stream().map(ind -> ind.getSolution()).collect(Collectors.toList());
  }

  @Override
  public String getName() {
    return "EMAS";
  }

  @Override
  public String getDescription() {
    return "EMAS";
  }
}
