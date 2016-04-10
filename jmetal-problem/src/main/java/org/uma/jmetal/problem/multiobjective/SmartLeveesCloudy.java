package org.uma.jmetal.problem.multiobjective;

/**
 * Created by adamzima on 10/04/16.
 */
public class SmartLeveesCloudy extends SmartLevees {
  @Override
  protected void setProblemName() {
    setName("SmartLeveesCloudy");
  }

  @Override
  protected double modemEEfactor(int modem) {
    switch (modem) {
      case 0:
        return 1.0;
      case 1:
        return 0.0;
    }
    return 0.0;
  }
}
