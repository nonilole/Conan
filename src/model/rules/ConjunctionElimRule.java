package model.rules;

public class ConjunctionElimRule implements Rule{
	private int type;
	private Integer reference;
	
	public ConjunctionElimRule(int type){
		super();
		if( type != 1 && type != 2) throw new IllegalArgumentException("ConjunctionElim(int type): type must be 1 or 2");
		this.type = type;
	}
	
	public ConjunctionElimRule(int type, int premise){
		super();
		if( type != 1 && type != 2) throw new IllegalArgumentException("ConjunctionElim(int type): type must be 1 or 2");
		this.type = type;
		this.reference = premise;
	}

	public int getType() {
		return type;
	}

	public Integer getPremise() {
		return reference;
	}

	public void setPremise(Integer premise) {
		this.reference = premise;
	}

	@Override
	public boolean hasCompleteInfo() {
		//type variable is guaranteed in constructor
		return reference != null;
	}
	
	@Override
	public String toString(){
	  return String.format("ConjunctionElimRule%s  (%s)",type,reference);
	}
}
