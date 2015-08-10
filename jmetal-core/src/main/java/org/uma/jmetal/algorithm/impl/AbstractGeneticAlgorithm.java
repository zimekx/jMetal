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

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.Solution;

import java.util.List;

/**
 * Created by ajnebro on 26/10/14.
 * @author Antonio J. Nebro
 * @author Juan J. Durillo (modified on 10/08/2015, adding constructors and protected fields)
 * 
 */
public abstract class AbstractGeneticAlgorithm<S extends Solution<?>, Result> extends AbstractEvolutionaryAlgorithm<S, Result> {
  private SelectionOperator<List<S>, S> selectionOperator ;
  private CrossoverOperator<S> crossoverOperator ;
  private MutationOperator<S> mutationOperator ;
  
  public AbstractGeneticAlgorithm(SelectionOperator<List<S>,S> selectionOperator, 
		  						  CrossoverOperator<S> crossoverOperator, 
		  						  MutationOperator<S> mutationOperator) {
	  this.selectionOperator = selectionOperator;
	  this.crossoverOperator = crossoverOperator;
	  this.mutationOperator  = mutationOperator;
	  
  }
  
  public SelectionOperator<List<S>,S> getSelectionOperator() {
	  return this.selectionOperator;
  }
  
  public CrossoverOperator<S> getCrossoverOperator() {
	  return this.crossoverOperator;
  }
  
  public MutationOperator<S>  getMutationOperator() {
	  return this.mutationOperator;
  }
  
}
