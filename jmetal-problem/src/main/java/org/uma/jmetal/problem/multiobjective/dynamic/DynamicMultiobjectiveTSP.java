package org.uma.jmetal.problem.multiobjective.dynamic;

import org.uma.jmetal.problem.impl.AbstractIntegerPermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;

/**
 * Version of the multi-objective TSP aimed at being solving dynamically.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class DynamicMultiobjectiveTSP extends AbstractIntegerPermutationProblem {
  private int         numberOfCities ;
  private double [][] distanceMatrix ;
  private double [][] costMatrix;

  public DynamicMultiobjectiveTSP(int numberOfCities,
                                  double[][] distanceMatrix,
                                  double[][] costMatrix) {
    this.numberOfCities = numberOfCities ;
    this.distanceMatrix = distanceMatrix ;
    this.costMatrix = costMatrix ;

    setName("DMoTSP");
    setNumberOfVariables(numberOfCities);
    setNumberOfObjectives(2);
    setNumberOfConstraints(0);
  }

  @Override
  public synchronized void evaluate(PermutationSolution<Integer> solution) {
    double fitness1   ;
    double fitness2   ;

    fitness1 = 0.0 ;
    fitness2 = 0.0 ;

    for (int i = 0; i < (numberOfCities - 1); i++) {
      int x ;
      int y ;

      x = solution.getVariableValue(i) ;
      y = solution.getVariableValue(i+1) ;

      fitness1 += distanceMatrix[x][y] ;
      fitness2 += costMatrix[x][y];
    }
    int firstCity ;
    int lastCity  ;

    firstCity = solution.getVariableValue(0) ;
    lastCity = solution.getVariableValue(numberOfCities - 1) ;

    fitness1 += distanceMatrix[firstCity][lastCity] ;
    fitness2 += costMatrix[firstCity][lastCity];

    solution.setObjective(0, fitness1);
    solution.setObjective(1, fitness2);
  }

  @Override
  public int getPermutationLength() {
    return numberOfCities;
  }

  /* Getters/Setters */
  public synchronized double[][] getDistanceMatrix() {
    return distanceMatrix;
  }

  public synchronized double[][] getCostMatrix() {
    return costMatrix;
  }

  public synchronized void setNumberOfCities(int numberOfCities) {
    this.numberOfCities = numberOfCities ;
  }

  public synchronized void setCostMatrix(double[][] costMatrix) {
    this.costMatrix = costMatrix ;
  }

  public synchronized void setDistanceMatrix(double[][] distanceMatrix) {
    this.distanceMatrix = distanceMatrix;
  }
}
