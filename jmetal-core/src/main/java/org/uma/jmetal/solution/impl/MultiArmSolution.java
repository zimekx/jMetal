package org.uma.jmetal.solution.impl;

import org.uma.jmetal.problem.impl.MultiArmBanditProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;


/**
 * @author juanjo
 * email: juanjod@gmail.com
 * This class provides an encapsulation for solutions to a multi-arm bandit
 * problem. The class uses an instance of multi-arm bandit problem 
 *
 */
public class MultiArmSolution implements DoubleSolution {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final MultiArmBanditProblem problem;	
	private 	  DefaultDoubleSolution solution;
	
	
	
	public MultiArmSolution(MultiArmBanditProblem problem) {			
		this.problem 	= problem;		
		this.solution 	= new DefaultDoubleSolution(problem.getWrapped());
	}

	public MultiArmSolution(MultiArmSolution solution) {
		this.problem 	 = solution.problem;
		this.solution 	 = solution.solution.copy();		
	}

	@Override
	public void setObjective(int index, double value) {
		this.solution.setObjective(index, value);		
	}

	@Override
	public double getObjective(int index) {
		return this.solution.getObjective(index);
	}

	@Override
	public Double getVariableValue(int index) {
		return solution.getVariableValue(this.problem.getSelectedArms().get(index));		
	}

	@Override
	public void setVariableValue(int index, Double value) {
		solution.setVariableValue(this.problem.getSelectedArms().get(index), value);		
	}

	@Override
	public String getVariableValueString(int index) {
		return solution.getVariableValueString(this.problem.getSelectedArms().get(index));
	}

	@Override
	public int getNumberOfVariables() {
		return problem.getNumberOfVariables();
	}

	@Override
	public int getNumberOfObjectives() {
		return problem.getNumberOfObjectives();
	}

	@Override
	public Solution<Double> copy() {
		return new MultiArmSolution(this);
	}

	@Override
	public void setAttribute(Object id, Object value) {
		solution.setAttribute(id, value);		
	}

	@Override
	public Object getAttribute(Object id) {
		return solution.getAttribute(id);
	}

	@Override
	public Double getLowerBound(int index) {
		return this.problem.getWrapped().getLowerBound(this.problem.getSelectedArms().get(index));
	}

	@Override
	public Double getUpperBound(int index) {
		return this.problem.getWrapped().getUpperBound(this.problem.getSelectedArms().get(index));
	}
	
	public DoubleSolution getInternalSolution() {
		return solution;
	}
}
