package org.uma.jmetal.algorithm.impl;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by adamzima on 24/04/16.
 */
public abstract class AbstractEmasAlgorithm<S extends Solution<?>, R, A extends AbstractEmasAgent> implements Algorithm<R> {
  private List<A> population;

  protected List<A> getPopulation() {
    return population;
  }

  protected abstract void initProgress();

  protected abstract void updateProgress();

  protected abstract boolean isStoppingConditionReached();

  protected abstract List<A> createInitialPopulation();

  @Override
  public void run() {
    population = createInitialPopulation();
    int i = 2;
    initProgress();
    updateProgress();

    System.out.println(population.size());
    while (!isStoppingConditionReached()) {
      System.out.println("Iteration: " + i++);

      int it = 0;
      while(it < population.size()) {
        A agent = population.get(it);
        agent.executeLifeStep(population);
        it++;
      }

      System.out.println("Population size: " + population.size());
      updateProgress();
    }
  }
}
