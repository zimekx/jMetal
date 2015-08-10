//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.


package org.uma.jmetal.algorithm.impl;

import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Abstract class representing differential evolution (DE) algorithms
 * @author Antonio J. Nebro
 * @author Juan J. Durillo (a.k.a Juanjo)
 * 
 * @version 1.0
 * @version 1.1
 * Juanjo added a constructor that forces a AbstractDifferentialEvolution to be created always
 * providing the crossover and selection operators. 
 */
public abstract class AbstractDifferentialEvolution<Result> extends AbstractEvolutionaryAlgorithm<DoubleSolution, Result> {
  private DifferentialEvolutionCrossover crossoverOperator ;
  private DifferentialEvolutionSelection selectionOperator ;
 
  /**
   * This constructor forces any DifferentialEvolution algorithm 
   * @param crossoverOperator
   * @param selectionOperator
   */
  		 
  public AbstractDifferentialEvolution(DifferentialEvolutionCrossover crossoverOperator, DifferentialEvolutionSelection selectionOperator) {
	  super();
	  this.crossoverOperator = crossoverOperator;
	  this.selectionOperator = selectionOperator;
  }

/**
   * Returns the crossover operator for this differential evolution algorithm
   * @return
   */
  public DifferentialEvolutionCrossover getCrossoverOperator() {
	return crossoverOperator;
  }

  /**
   * Returns the selection operator for this differential evolution algorithm
   * @return
   */
  public DifferentialEvolutionSelection getSelectionOperator() {
		return selectionOperator;
  }

  
}
