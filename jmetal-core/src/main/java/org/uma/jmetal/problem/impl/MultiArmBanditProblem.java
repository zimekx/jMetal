package org.uma.jmetal.problem.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.solution.impl.MultiArmSolution;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
/**
 * 
 * @author Juanjo 
 * juanjod@gmail.com
 * @version 1.0
 *
 * This class implements a first version of the MultiArmBanditProblem approach. 
 * It aims to be a wrapper of normal problems. 
 * This class overload some of the methods inherited by AbstractDoubleProblem to 
 *  
 */
public class MultiArmBanditProblem extends AbstractDoubleProblem {

	private static 	final long 		serialVersionUID 			= 1L;		
	private 		final 			AbstractDoubleProblem wrappedProblem;
	private 		final int		numberOfArms;
	private			List<Double> 	probabilities				;
	private			List<Integer>	selectedArms				;
	private			List<Integer>   producedDominatingSolutions ;
	private			NonDominatedSolutionListArchive<DoubleSolution> archive = new NonDominatedSolutionListArchive<>();
		
	/**
	 * @param wrappedProblem The problem wrapped by this class
	 */
	public MultiArmBanditProblem(AbstractDoubleProblem wrappedProblem, int numberOfArms) {
		this.wrappedProblem = wrappedProblem;
		this.numberOfArms   = (numberOfArms > wrappedProblem.getNumberOfVariables())?
								wrappedProblem.getNumberOfVariables():numberOfArms;
		this.probabilities  = new ArrayList<>(this.wrappedProblem.getNumberOfVariables());
		this.producedDominatingSolutions = new ArrayList<>();
		for (int i = 0; i < this.wrappedProblem.getNumberOfVariables(); i++) {
			this.probabilities.add(1.0/this.wrappedProblem.getNumberOfVariables());
			this.producedDominatingSolutions.add(0);
		}
		this.selectedArms	= new ArrayList<>(this.numberOfArms);	
		this.selectInitialArms();
	} // MultiArmBanditProblem	
	
	
	/**
	 * @return the abstractDoubleProblem wrapped by this class
	 */
	public AbstractDoubleProblem getWrapped() {
		return this.wrappedProblem;
	} // AbstractDoubleProblem
		
	/**
	 * @return the arms currently selected by this method
	 */
	public List<Integer> getSelectedArms(){
		return this.selectedArms;
	} // getSelectedArms
		
	@Override
	public void evaluate(DoubleSolution solution) {
		
		MultiArmSolution mas = (MultiArmSolution)solution;
		wrappedProblem.evaluate(mas.getInternalSolution());
		if (archive.add(solution)) 
			for (Integer index : this.selectedArms)
				this.producedDominatingSolutions.set(index, this.producedDominatingSolutions.get(index)+1);
		
		
		this.updateArms();
	}
	
	@Override
	public int getNumberOfVariables() {
		return this.numberOfArms;
	}
	
	@Override
	public int getNumberOfObjectives() {
		return wrappedProblem.getNumberOfObjectives();
	}
	
	private void selectInitialArms() {
		Set<Integer> alreadySelected = new TreeSet<>();
		for (int i = 0; i < this.numberOfArms; i++) {
			int candidate = JMetalRandom.getInstance().nextInt(0, wrappedProblem.getNumberOfVariables()-1);
			while (alreadySelected.contains(candidate))
				candidate = JMetalRandom.getInstance().nextInt(0, wrappedProblem.getNumberOfVariables()-1);
			alreadySelected.add(candidate);
			this.selectedArms.add(candidate);
		}
	}
	
	/* 
	 * This method select the arms using an epsilon approach
	 * Epsilon value set to 0.2 (20% of the time we'll be pulling the best arm)
	 */
	private void greedyUpdateArms() {
		//create a list of IndexValue values
		List<IndexAndValue> indexAndValues = new ArrayList<>(this.producedDominatingSolutions.size());
		for (int i = 0; i < this.producedDominatingSolutions.size();i++)
			indexAndValues.add(new IndexAndValue(i,this.producedDominatingSolutions.get(i)));
	
		Collections.sort(indexAndValues,new Comparator<IndexAndValue>() {
			@Override
			public int compare(IndexAndValue o1, IndexAndValue o2) {
				if (o1.getValue() > o2.getValue())
					return -1;
				else if (o1.getValue() < o2.getValue())
					return 1;
				else
					return 0;
			}
		});
		
		this.selectedArms.clear();
		while (selectedArms.size()<this.numberOfArms) {
			if (JMetalRandom.getInstance().nextDouble() < 0.5) {
				this.selectedArms.add(indexAndValues.remove(0).getIndex());
			} else {
				int randomIndex = JMetalRandom.getInstance().nextInt(0, indexAndValues.size()-1);
				this.selectedArms.add(indexAndValues.remove(randomIndex).getIndex());
			}
		}
	}
	
	private class IndexAndValue {
		private int index;
		private int value;
		public IndexAndValue(int index, int value) {
			this.index = index;
			this.value = value;
		}
		public int getIndex() {
			return this.index;
		}
		public int getValue() {
			return this.value;
		}
	}
	
	
	private void updateArms() {
		this.greedyUpdateArms();
	}
	
	public void printProbability() {
		for (Double pro : this.probabilities)
			System.out.print(pro+",");
		System.out.println();
	}
	
	  @Override
	  public DoubleSolution createSolution() {
	    return new MultiArmSolution(this)  ;
	  }
} 
