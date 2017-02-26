package model;

import javafx.scene.control.Tab;
import view.ProofView;

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
