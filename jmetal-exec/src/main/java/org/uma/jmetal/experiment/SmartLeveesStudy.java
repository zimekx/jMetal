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

package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.SmartLevees;
import org.uma.jmetal.problem.multiobjective.SmartLeveesCloudy;
import org.uma.jmetal.problem.multiobjective.SmartLeveesSunny;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of experimental study based on solving the ZDT problems with four versions of NSGA-II, each
 * of them applying a different crossover probability (from 0.7 to 1.0).
 * <p/>
 * This experiment assumes that the reference Pareto front are known, so the names of files containing
 * them and the directory where they are located must be specified.
 * <p/>
 * Six quality indicators are used for performance assessment.
 * <p/>
 * The steps to carry out the experiment are:
 * 1. Configure the experiment
 * 2. Execute the algorithms
 * 3. Compute the quality indicators
 * 4. Generate Latex tables reporting means and medians
 * 5. Generate Latex tables with the result of applying the Wilcoxon Rank Sum Test
 * 6. Generate Latex tables with the ranking obtained by applying the Friedman test
 * 7. Generate R scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class SmartLeveesStudy {
  private static final int INDEPENDENT_RUNS = 1;
  private static final int ITERATIONS = 250;
  private static final int POPULATION_SIZE = 100;

  public static void main(String[] args) throws IOException {
//    if (args.length != 1) {
//      throw new JMetalException("Missing argument: experiment base directory") ;
//    }
    String experimentBaseDirectory = "/Users/adamzima/semestr8/jmetal-5/jMetal/sm";

    List<Problem<IntegerSolution>> problemList = Arrays.<Problem<IntegerSolution>>asList(new SmartLeveesSunny(), new SmartLeveesCloudy());

    List<TaggedAlgorithm<List<IntegerSolution>>> algorithmList = configureAlgorithmList(problemList, INDEPENDENT_RUNS);

    List<String> referenceFrontFileNames = Arrays.asList("SmartLeveesSunny.pf", "SmartLeveesCloudy.pf");

    Experiment<IntegerSolution, List<IntegerSolution>> experiment =
        new ExperimentBuilder<IntegerSolution, List<IntegerSolution>>("SmartLeveesStudy")
            .setAlgorithmList(algorithmList)
            .setProblemList(problemList)
            .setExperimentBaseDirectory(experimentBaseDirectory)
            .setOutputParetoFrontFileName("FUN")
            .setOutputParetoSetFileName("VAR")
            .setReferenceFrontDirectory("/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees_study/pareto_fronts")
            .setReferenceFrontFileNames(referenceFrontFileNames)
            .setIndicatorList(Arrays.asList(
                new PISAHypervolume<IntegerSolution>(), new InvertedGenerationalDistancePlus<IntegerSolution>()))
            .setIndependentRuns(INDEPENDENT_RUNS)
            .setIterations(ITERATIONS)
            .setNumberOfCores(8)
            .build();

    new ExecuteAlgorithms<>(experiment).run();
    new ComputeQualityIndicators<>(experiment).run();
//    new GenerateLatexTablesWithStatistics(experiment).run();
//    new GenerateWilcoxonTestTablesWithR<>(experiment).run();
//    new GenerateFriedmanTestTables<>(experiment).run();
//    new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).run();
    System.out.println("KONIEC");
  }

  /**
   * The algorithm list is composed of pairs {@link org.uma.jmetal.algorithm.Algorithm} + {@link org.uma.jmetal.problem.Problem} which form part of a
   * {@link org.uma.jmetal.util.experiment.util.TaggedAlgorithm}, which is a decorator for class {@link org.uma.jmetal.algorithm.Algorithm}. The {@link org.uma.jmetal.util.experiment.util.TaggedAlgorithm}
   * has an optional tag component, that can be set as it is shown in this example, where four variants of a
   * same algorithm are defined.
   *
   * @param problemList
   * @return
   */
  static List<TaggedAlgorithm<List<IntegerSolution>>> configureAlgorithmList(List<Problem<IntegerSolution>> problemList, int independentRuns) {
    List<TaggedAlgorithm<List<IntegerSolution>>> algorithms = new ArrayList<>();

    for (int run = 0; run < independentRuns; run++) {

      for (int i = 0; i < problemList.size(); i++) {
        Algorithm<List<IntegerSolution>> nsga_ii_algorithm = new NSGAIIBuilder<>(problemList.get(i), new IntegerSBXCrossover(1.0, 5),
            new IntegerPolynomialMutation(1.0 / problemList.get(i).getNumberOfVariables(), 10.0))
            .setMaxEvaluations(ITERATIONS * POPULATION_SIZE + 1)
            .setPopulationSize(POPULATION_SIZE)
            .build();
        algorithms.add(new TaggedAlgorithm<>(nsga_ii_algorithm, "NSGAII", problemList.get(i), run));

        Algorithm<List<IntegerSolution>> spea_2_algorithm = new SPEA2Builder<>(problemList.get(i), new IntegerSBXCrossover(1.0, 5),
            new IntegerPolynomialMutation(1.0 / problemList.get(i).getNumberOfVariables(), 10.0))
            .setMaxIterations(ITERATIONS + 1)
            .setPopulationSize(POPULATION_SIZE)
            .build();
        algorithms.add(new TaggedAlgorithm<>(spea_2_algorithm, "SPEA2", problemList.get(i), run));

      }
    }
    return algorithms;
  }
}