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
import org.uma.jmetal.algorithm.multiobjective.emas.EmasBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSOBuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.zdt.*;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of experimental study based on solving the ZDT problems with five algorithms: NSGAII, SPEA2, MOCell,
 * SMPSO and AbYSS
 *
 * This experiment assumes that the reference Pareto front are known, so the names of files containing
 * them and the directory where they are located must be specified.
 *
 * Six quality indicators are used for performance assessment.
 *
 * The steps to carry out the experiment are:
 * 1. Configure the experiment
 * 2. Execute the algorithms
 * 3. Compute que quality indicators
 * 4. Generate Latex tables reporting means and medians
 * 5. Generate R scripts to produce latex tables with the result of applying the Wilcoxon Rank Sum Test
 * 6. Generate Latex tables with the ranking obtained by applying the Friedman test
 * 7. Generate R scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class EmasZdtStudy {
  private static final int INDEPENDENT_RUNS = 1 ;
  private static final int ITERATIONS = 250;
  private static final int POPULATION_SIZE = 100;

  public static void main(String[] args) throws IOException {
    String experimentBaseDirectory = "/Users/adamzima/semestr8/jmetal-5/jMetal/experiment";

    List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(new ZDT1()); //, new ZDT2(), new ZDT3(), new ZDT4(), new ZDT6()) ;

    List<String> referenceFrontFileNames = Arrays.asList("ZDT1.pf"); //, "ZDT2.pf", "ZDT3.pf", "ZDT4.pf", "ZDT6.pf") ;

    List<TaggedAlgorithm<List<DoubleSolution>>> algorithmList = configureAlgorithmList(problemList, INDEPENDENT_RUNS) ;

    Experiment<DoubleSolution, List<DoubleSolution>> experiment =
        new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("ZDTStudy")
            .setAlgorithmList(algorithmList)
            .setProblemList(problemList)
            .setReferenceFrontDirectory("/Users/adamzima/semestr8/jmetal-5/jMetal/jmetal-core/src/main/resources/pareto_fronts/")
            .setReferenceFrontFileNames(referenceFrontFileNames)
            .setExperimentBaseDirectory(experimentBaseDirectory)
            .setOutputParetoFrontFileName("FUN")
            .setOutputParetoSetFileName("VAR")
            .setIndicatorList(Arrays.asList(
                new PISAHypervolume<DoubleSolution>(), new InvertedGenerationalDistancePlus<DoubleSolution>()))
            .setIndependentRuns(INDEPENDENT_RUNS)
            .setIterations(ITERATIONS)
            .setNumberOfCores(1)
            .build();

    new ExecuteAlgorithms<>(experiment).run();
    new ComputeQualityIndicators<>(experiment).run() ;
//    new GenerateLatexTablesWithStatistics(experiment).run() ;
//    new GenerateWilcoxonTestTablesWithR<>(experiment).run() ;
//    new GenerateFriedmanTestTables<>(experiment).run();
//    new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).setDisplayNotch().run() ;
  }

  /**
   * The algorithm list is composed of pairs {@link org.uma.jmetal.algorithm.Algorithm} + {@link org.uma.jmetal.problem.Problem} which form part of a
   * {@link org.uma.jmetal.util.experiment.util.TaggedAlgorithm}, which is a decorator for class {@link org.uma.jmetal.algorithm.Algorithm}.
   *
   * @param problemList
   * @return
   */
  static List<TaggedAlgorithm<List<DoubleSolution>>> configureAlgorithmList(
      List<Problem<DoubleSolution>> problemList,
      int independentRuns) {
    List<TaggedAlgorithm<List<DoubleSolution>>> algorithms = new ArrayList<>() ;

    for (int run = 0; run < independentRuns; run++) {
      for (int i = 0; i < problemList.size(); i++) {
        Algorithm<List<DoubleSolution>> emas_algorithm = new EmasBuilder<>(problemList.get(i), new SBXCrossover(1.0, 5),
            new PolynomialMutation(1.0 / problemList.get(i).getNumberOfVariables(), 10.0), new DominanceComparator())
              .setMaxIterations(ITERATIONS + 1)
              .setPopulationSize(POPULATION_SIZE)
              .setStartingEnergy(5)
              .setReproductionThreshold(6)
              .setMeetingCost(2)
              .setReproductionCost(2)
              .setDeathThreshold(2)
              .setNeighbourhoodThreshold(0.25)
              .build();
        algorithms.add(new TaggedAlgorithm<>(emas_algorithm, "EMAS", problemList.get(i), run));

      }
    }
    return algorithms ;
  }
}
