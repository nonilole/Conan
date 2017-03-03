package view;

import javafx.scene.control.Tab;

public class ProofTab extends Tab {
    private ProofView view;
    public ProofTab(String s, ProofView view) {
        super(s);
        this.view = view;
    }

    public ProofView getView() {
        return this.view;
    }
}
