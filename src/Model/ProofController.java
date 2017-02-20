package Model;

class ProofController{
    public void addRow(String formula,String rule){}//covered by insertRow?
    public void deleteRow(int rowNumber, boolean cascade){}
    public void insertRow(String formula,String rule,int rowNumber){}
    public void updateRow(String formula, String rule, int rowNumber){}
    public void saveProof(String filepath){}
    public void loadProof(String filepath){}
    public boolean verifyProof(int start){return true;}
    public boolean verifyRow(int rowNumber){return true;}
    public void closeBox(){}
    public void verifyConclusion(){}
    public void registerProofListener(/*ProofListener pl*/){}
    public Rule createCustomRule(){return null;}
}
