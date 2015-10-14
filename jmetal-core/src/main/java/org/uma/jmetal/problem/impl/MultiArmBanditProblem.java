package org.uma.jmetal.problem.impl;

import java.util.ArrayList;
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
	private			int				cnt							= 0;
	private			List<Double> 	probabilities				;
	private			List<Integer>	selectedArms				;
	private			List<Integer>   producedDominatingSolutions ;
	private			NonDominatedSolutionListArchive<DoubleSolution> archive =
					new NonDominatedSolutionListArchive<>();
		
	/**
	 * Creates an instance of the MultiArmBanditProblem using as
	 * input another AbstractDoubleProblem
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
		cnt++;
		if (archive.add(solution)) 
			for (Integer index : this.selectedArms)
				this.producedDominatingSolutions.set(index, this.producedDominatingSolutions.get(index)+1);
		
		
		if (cnt%500==0)
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
	
	private void updateArms() {
		Set<Integer> alreadySelected = new TreeSet<>();
		int total = 0;
		for (Integer added : this.producedDominatingSolutions)
			total += added;
		
		Double totalProbability = 0.0;
		
		for (int i = 0; i < this.probabilities.size(); i++) {
			this.probabilities.set(i, this.probabilities.get(i) + (double)this.producedDominatingSolutions.get(i)/(double)total);
			totalProbability += this.probabilities.get(i);
		}
				
		
		this.selectedArms.clear();
		while (this.selectedArms.size() < this.numberOfArms) {
			
			int 	candidate = 0;
			double 	randomProbability = JMetalRandom.getInstance().nextDouble(0.0, totalProbability);
			double  accumulated = this.probabilities.get(0);
			while (accumulated < randomProbability) {				
				accumulated += this.probabilities.get(++candidate);
			}
			if (candidate == this.wrappedProblem.getNumberOfVariables())
				candidate--;
			
			if (!alreadySelected.contains(candidate)) {
				this.selectedArms.add(candidate);
				alreadySelected.add(candidate);
			}								
		}
		System.out.print("Selected arms: ");
		for (Integer selected : this.selectedArms)
			System.out.print(selected+" ");
		System.out.println();
	}
	
	  @Override
	  public DoubleSolution createSolution() {
	    return new MultiArmSolution(this)  ;
	  }
} 
