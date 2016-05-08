package org.uma.jmetal.algorithm.impl;

import org.uma.jmetal.solution.Solution;

import java.util.List;

/**
 * Created by adamzima on 24/04/16.
 */
public abstract class AbstractEmasAgent<S extends Solution> {
  protected final int deathThreshold;
  protected final int reproductionThreshold;
  protected final int meetingCost;
  protected final int reproductionCost;

  protected S solution;
  protected int energyLevel;

  protected AbstractEmasAgent(S solution, int startingEnergyLevel,
                              int deathThreshold, int reproductionThreshold,
                              int meetingCost, int reproductionCost) {
    this.solution = solution;
    this.energyLevel = startingEnergyLevel;
    this.deathThreshold = deathThreshold;
    this.reproductionThreshold = reproductionThreshold;
    this.meetingCost = meetingCost;
    this.reproductionCost = reproductionCost;
  }

  public abstract S getSolution();

  protected abstract void die(List<AbstractEmasAgent<S>> nextPopulation);

  protected abstract void meet(List<AbstractEmasAgent<S>> population);

  protected abstract void reproduce(List<AbstractEmasAgent<S>> population, List<AbstractEmasAgent<S>> nextPopulation);

  public int getEnergyLevel() {
    return energyLevel;
  }

  public void executeLifeStep(List<AbstractEmasAgent<S>> population, List<AbstractEmasAgent<S>> nextPopulation) {
    if (energyLevel <= this.deathThreshold) {
      die(nextPopulation);
      return;
    }

    meet(population);
    if (energyLevel > this.reproductionThreshold) {
      System.out.println("Reproduction");
      reproduce(population, nextPopulation);
    } else {
      nextPopulation.add(this);
    }
  }
}
