//  NMMin.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2014 Antonio J. Nebro
//
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
//
package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adam Zima on 16/04/02.
 * SmartLevees Problem
 *
 * Variable 1: Processing Interval (PI) 5/15//60/720/1440 - f(x) = 1 - x/1440
 * Variable 2: Measurement Interval (MI) 1/5/15/60/720/1440 - f(x) = 1 - x/1440
 * Variable 3: Computing Schedule (CI) cost-optimized/time-optimized
 * Variable 4: VM allocation (VM) conservative/aggresive
 * Variable 5: Modem (M) XBee/GPRS
 * Variable 6: Aggregation (A) on/half/off
 *
 * Objective 1: Operating Cost - OPC
 * Objective 2: Energy Efficiency - EE
 * Objective 3: Timeliness - TML
 */
public abstract class SmartLevees extends AbstractIntegerProblem {
  private static final int NUMBER_OF_VARIABLES = 6;
  private static final int NUMBER_OF_OBJECTIVES = 3;
  private static final HashMap<Integer, Double> PROCESSING_INTERVAL = new HashMap<Integer, Double>() {{
    put(0, 5.0);
    put(1, 15.0);
    put(2, 60.0);
    put(3, 720.0);
    put(4, 1440.0);
  }};

  private static final HashMap<Integer, Double> MEASUREMENT_INTERVAL = new HashMap<Integer, Double>() {{
    put(0, 1.0);
    put(1, 5.0);
    put(2, 15.0);
    put(3, 60.0);
    put(4, 720.0);
    put(5, 1440.0);
  }};

  public static final Integer [] LOWER_LIMIT = {0, 0, 0, 0, 0, 0};
  public static final Integer [] UPPER_LIMIT = {4, 5, 1, 1, 1, 2};

  public SmartLevees() {
    setNumberOfVariables(NUMBER_OF_VARIABLES);
    setNumberOfObjectives(NUMBER_OF_OBJECTIVES);
    setProblemName();

    List<Integer> lowerLimit = Arrays.asList(LOWER_LIMIT) ;
    List<Integer> upperLimit = Arrays.asList(UPPER_LIMIT) ;

    setLowerLimit(lowerLimit);
    setUpperLimit(upperLimit);
  }

  protected abstract void setProblemName();

  /** Evaluate() method */
  @Override
  public void evaluate(IntegerSolution solution) {
    int processingInterval = solution.getVariableValue(0);
    int measurementInterval = solution.getVariableValue(1);
    int computingSchedule = solution.getVariableValue(2);
    int vmAllocation = solution.getVariableValue(3);
    int modem = solution.getVariableValue(4);
    int aggregation = solution.getVariableValue(5);

    double opc = calculateOPC(processingInterval, computingSchedule, vmAllocation, modem);
    double ee = calculateEE(measurementInterval, modem, aggregation);
    double tml = calculateTML(processingInterval, measurementInterval, computingSchedule, vmAllocation, modem, aggregation);

    solution.setObjective(0, opc);
    solution.setObjective(1, -ee);
    solution.setObjective(2, -tml);
  }

  private double calculateTML(int processingInterval, int measurementInterval, int computingSchedule, int vmAllocation, int modem, int aggregation) {
    double tml = 0.0;

    tml += 1.0 - (PROCESSING_INTERVAL.get(processingInterval) / 1440.0);
    tml += 1.0 - (MEASUREMENT_INTERVAL.get(measurementInterval) / 1440.0);
    tml += computingSchedule;
    tml += vmAllocation;
    tml += modem;
    switch (aggregation) {
      case 0:
        tml += 0.0;
        break;
      case 1:
        tml += 0.5;
        break;
      case 2:
        tml += 1.0;
        break;
    }

    return tml / 6.0;
  }

  private double calculateEE(int measurementInterval, int modem, int aggregation) {
    double ee = 0.0;
    ee += MEASUREMENT_INTERVAL.get(measurementInterval) / 1440.0;

    ee += modemEEfactor(modem);

    switch(aggregation) {
      case 0:
        ee += 1.0;
        break;
      case 1:
        ee += 0.5;
        break;
      case 2:
        ee += 0.0;
        break;
    }

    return ee / 3.0;
  }

  protected abstract double modemEEfactor(int modem);

  private double calculateOPC(int processingInterval, int computingSchedule, int vmAllocation, int modem) {
    double opc = 0.0;
    opc += 1.0 - (PROCESSING_INTERVAL.get(processingInterval) / 1440.0);
    opc += computingSchedule;
    opc += vmAllocation;
    opc += modem;

    return opc / 4.0;
  }
}
