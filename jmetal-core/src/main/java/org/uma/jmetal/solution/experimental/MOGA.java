package org.uma.jmetal.solution.experimental;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.Operator;

/**
 * Created by ajnebro on 28/5/15.
 */
public class MOGA<Solution> implements Algorithm<Solution> {
  private Operator<Solution, Solution> mutation ;
  private Operator<Solution, Solution> crossover ;
  private Operator<Solution, Solution> selection ;


  @Override public void run() {

  }

  @Override public Solution getResult() {
    return null;
  }
}
