package model.rules;

public class ImplicationIntroRule implements Rule {

	private Intervall premiseIntervall;

	public ImplicationIntroRule(){
		super();
	}
	
	public ImplicationIntroRule(Intervall intervall){
		super();
		premiseIntervall = intervall;
	}
	
	public Intervall getPremiseIntervall() {
		return premiseIntervall;
	}

	public void setPremiseIntervall(Intervall premiseIntervall) {
		this.premiseIntervall = premiseIntervall;
	}
	
	@Override
	public boolean hasCompleteInfo() {
		return premiseIntervall != null;
	}
	
	@Override
	public String toString(){
		return String.format("→-I (%s)", premiseIntervall);
	}

}
