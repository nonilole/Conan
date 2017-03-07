package model;

public interface ProofListener {
    //Move to proper folder, should be in Model!
    public void boxOpened();
    public void boxClosed();
    public void rowUpdated(boolean wellFormed, int lineNo);
    public void conclusionReached(boolean correct, int lineNo);
    public void rowDeleted();
    public void rowInserted(String formula, String rule);
}

