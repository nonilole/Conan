package model.rules;

public class ConjunctionElimRule extends Rule{
	private int type;
	private Integer premise;
	
	public ConjunctionElimRule(int type){
		super();
		if( type != 1 && type != 2) throw new IllegalArgumentException("ConjunctionElim(int type): type must be 1 or 2");
		this.type = type;
	}
	
	public ConjunctionElimRule(int type, int premise){
		super();
		if( type != 1 && type != 2) throw new IllegalArgumentException("ConjunctionElim(int type): type must be 1 or 2");
		this.type = type;
		this.premise = premise;
	}

	public int getType() {
		return type;
	}

	public Integer getPremise() {
		return premise;
	}

	public void setPremise(Integer premise) {
		this.premise = premise;
	}

	@Override
	public boolean hasCompleteInfo() {
		//type variable is guaranteed in constructor
		return premise != null;
	}
}
