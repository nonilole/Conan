package model;

public interface ProofListener {
    public void boxInserted(int lineNo);
    public void boxRemoved(int lineNo);
    public void rowUpdated(String newText, boolean wellFormed, int lineNo);
    public void conclusionReached(boolean correct, int lineNo);
    public void rowVerified(boolean verified, int lineNo);
    public void rowDeleted(int rowNr);
    public void rowInserted(int rowNo, BoxReference order, int depth);
    public void deletedRowAfterBox(int rowNo);
    public void rowAdded();
    public void updateErrorStatus(int lineNo, String message);
	public void updateParsingStatus(int lineNo, String message);
	public void updateStatus();
	public void premisesUpdated(String newPremisesStr);

    static class RowInfo{
    	public final String expression;
    	public final String rule;
    	public final String ref1;
    	public final String ref2;
    	public final String ref3;
    	public final boolean startBox;
    	public final boolean endBox;
    	public final boolean wellformed;
    	public final boolean verified;
    	
    	public RowInfo(String expression,String rule,String ref1,String ref2,String ref3,
    			boolean startBox,boolean endBox, boolean wellformed, boolean verified){
    		this.expression = expression;
    		this.rule = rule;
    		this.ref1 = ref1;
    		this.ref2 = ref2;
    		this.ref3 = ref3;
    		this.startBox = startBox;
    		this.endBox =  endBox;
    		this.wellformed = wellformed;
    		this.verified = verified;
    	}
    	
    	@Override
    	public String toString(){
    		return String.format("%s :: %s (%s)(%s)(%s)||startBox: %b, endBox: %b, wellformed: %b, verified: %b " , 
    				expression, rule, ref1,ref2,ref3, startBox,endBox,wellformed,verified);
    	}
    }
}

