package org.uma.jmetal.algorithm.impl;

import org.uma.jmetal.solution.Solution;

import java.util.Comparator;
import java.util.List;

/**
 * Created by adamzima on 24/04/16.
 */
public abstract class AbstractEmasAgent<S extends Solution> {
  protected final int deathThreshold;
  protected final int reproductionThreshold;
  protected final int meetingCost;
  protected final int reproductionCost;

  public int meetingCounter;
  public int neighbourCounter;
  public int dominatedCounter;

  public final double neighbourhoodThreshold;

  protected S solution;
  protected int energyLevel;

  public final Comparator<S> dominanceComparator;


  protected AbstractEmasAgent(S solution, int startingEnergyLevel,
                              int deathThreshold, int reproductionThreshold,
                              int meetingCost, int reproductionCost, Comparator<S> dominanceComparator, double neighbourhoodThreshold) {
    this.solution = solution;
    this.energyLevel = startingEnergyLevel;
    this.deathThreshold = deathThreshold;
    this.reproductionThreshold = reproductionThreshold;
    this.meetingCost = meetingCost;
    this.reproductionCost = reproductionCost;
    this.meetingCounter = 0;
    this.neighbourCounter = 0;
    this.dominatedCounter = 0;
    this.dominanceComparator = dominanceComparator;
    this.neighbourhoodThreshold = neighbourhoodThreshold;
  }

  public abstract S getSolution();

  protected abstract void die();

  protected abstract void meet(List<AbstractEmasAgent<S>> population);

  protected abstract void reproduce(List<AbstractEmasAgent<S>> population);

  public int getEnergyLevel() {
    return energyLevel;
  }

  public void executeLifeStep(List<AbstractEmasAgent<S>> population) {
    if (this.energyLevel <= this.deathThreshold) {
      die();
      return;
    }

    meet(population);
    if (this.energyLevel > this.reproductionThreshold) {
      System.out.println("Reproduction");
      reproduce(population);
    }
  }

  protected double getNeighbourhoodDensity() {
    return (double) this.neighbourCounter / (double) Math.max(this.meetingCounter, 1);
  }

  private int compareDominatedCounter(AbstractEmasAgent<S> otherAgent) {
    if (this.dominatedCounter < otherAgent.dominatedCounter) return -1;
    if (this.dominatedCounter > otherAgent.dominatedCounter) return 1;
    return 0;
  }

  private int compareNeighbourhoodDensity(AbstractEmasAgent<S> otherAgent) {
    if (this.getNeighbourhoodDensity() < otherAgent.getNeighbourhoodDensity()) return -1;
    if (this.getNeighbourhoodDensity() > otherAgent.getNeighbourhoodDensity()) return 1;
    return 0;
  }

  public int compare(AbstractEmasAgent<S> otherAgent) {
    int compareResult = dominanceComparator.compare(this.solution, otherAgent.getSolution());
    if (compareResult == 0) {
      compareResult = compareNeighbourhoodDensity(otherAgent);
      if (compareResult == 0) {
        compareResult = compareDominatedCounter(otherAgent);
      }
    }
    return compareResult;
  }

  public int compareReproduction(AbstractEmasAgent<S> otherAgent) {
    int compareResult = dominanceComparator.compare(this.solution, otherAgent.getSolution());
    if (compareResult == 0) {
      if (this.meetingCounter > 0 && this.neighbourCounter == 0) {
        compareResult = -1;
      } else if (otherAgent.meetingCounter > 0 && otherAgent.neighbourCounter == 0) {
        compareResult = 1;
      }
    }
    return compareResult;
  }

  public void printStats() {
    System.out.println(
        "\t" + this.solution.getObjective(0) +
        "\t" + this.solution.getObjective(1) +
        "\t" + this.energyLevel +
        "\t" + this.getNeighbourhoodDensity() +
        "\t" + this.neighbourCounter +
        "\t" + this.meetingCounter +
        "\t" + this.dominatedCounter
    );
  }
}
