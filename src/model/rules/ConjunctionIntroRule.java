package model.rules;

public class ConjunctionIntroRule implements Rule{

	//indexes of the premises
	private Integer premise1;
	private Integer premise2;
	
	public ConjunctionIntroRule(){ super(); }
	
	public ConjunctionIntroRule(int premise1Index, int premise2Index){
		super();
		premise1 = premise1Index;
		premise2 = premise2Index;
	}
	
	@Override
	public boolean hasCompleteInfo() {
		return premise1 != null && premise2 != null;
	}

	public Integer getPremise1() {
		return premise1;
	}

	public void setPremise1(int premise1) {
		this.premise1 = premise1;
	}

	public Integer getPremise2() {
		return premise2;
	}

	public void setPremise2(int premise2) {
		this.premise2 = premise2;
	}
	
	@Override
	public String toString(){
		String p1 = premise1 == null ? "" : premise1.toString();
		String p2 = premise2 == null ? "" : premise2.toString();
		return String.format("∧-I (%s),(%s)",p1,p2);
	}
	
}
