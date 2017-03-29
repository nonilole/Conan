package model.rules;

public class ImplicationIntroRule extends Rule {

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
		return premiseIntervall == null;
	}

}
