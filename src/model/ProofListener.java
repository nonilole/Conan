package model;

public interface ProofListener {
    public void boxOpened();
    public void boxClosed();
    public void boxInserted(int lineNo);
    public void boxRemoved(int lineNo);
    public void rowUpdated(boolean wellFormed, int lineNo);
    public void conclusionReached(boolean correct, int lineNo);
    public void rowDeleted(int rowNr);
    public void rowInserted(int rowNo, BoxReference order);
    public void rowAdded();
    
}

