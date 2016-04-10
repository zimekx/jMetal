package org.uma.jmetal.problem.multiobjective;

/**
 * Created by adamzima on 10/04/16.
 */
public class SmartLeveesSunny extends SmartLevees {

  @Override
  protected void setProblemName() {
    setName("SmartLeveesSunny");
  }

  @Override
  protected double modemEEfactor(int modem) {
    return 1.0;
  }
}
