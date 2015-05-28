package org.uma.jmetal.solution.experimental.problem.impl;

import org.uma.jmetal.solution.SolutionBuilder;
import org.uma.jmetal.solution.SolutionEvaluator;
import org.uma.jmetal.solution.experimental.problem.ExpProblem;

import java.util.Collection;

/**
 * Created by ajnebro on 28/5/15.
 *
 * Schaffer is a double problem
 */
public class ExpSchaffer<Solution> implements
    ExpProblem<Solution>, SolutionEvaluator<Solution>, SolutionBuilder<Solution> {
  @Override public Collection<Variable<Solution, ?>> getVariables() {
    return null;
  }

  @Override public <Value> void prepare(Variable<Solution, Value> variable, Value value) {

  }

  @Override public Solution build() {
    return null;
  }

  @Override public Collection<Objective<Solution, ?>> getObjectives() {
    return null;
  }
}
