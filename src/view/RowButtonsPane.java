package view;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;

/**
 * Created by Rapie on 2017-03-31.
 */
public class RowButtonsPane extends FlowPane {
    Button deleteButton=new Button("X");
    Button insertBeforeButton=new Button("\u2BC5");
    Button insertAfterButton=new Button("\u2BC6");

    final Tooltip deleteTooltip = new Tooltip("Delete");
    final Tooltip insertBeforeTooltip = new Tooltip("Insert Above");
    final Tooltip insertAfterTooltip = new Tooltip("Insert Below");

    public RowButtonsPane(){
        this.setMaxWidth(80);
        deleteButton.setTooltip(deleteTooltip);
        insertBeforeButton.setTooltip(insertBeforeTooltip);
        insertAfterButton.setTooltip(insertAfterTooltip);

        this.getChildren().addAll(deleteButton,insertBeforeButton,insertAfterButton);
    }

    public Button getDeleteButton(){
        return deleteButton;
    }

    public Button getInsertBeforeButton(){
        return insertBeforeButton;
    }
    public Button getInsertAfterButton(){
        return insertAfterButton;
    }

}