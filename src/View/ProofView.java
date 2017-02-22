package View;
import java.util.Stack;

import Model.ProofListener;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class ProofView implements ProofListener{
	
	
    public void boxOpened(){}
    public void boxClosed(){}
    public void rowUpdated(){}
    public void conclusionReached(){}
    public void rowDeleted(){}
    public void rowInserted(){}
    
    public static void newProof(TabPane tabPane ) {
    	Tab tab = new Tab("hej");
    	
    	TableColumn tableColumnLeft = new TableColumn();
    	tableColumnLeft.setText("Expression");
    	tableColumnLeft.setEditable(true);
    	
    	
    	TableColumn tableColumnRight = new TableColumn();
    	tableColumnRight.setText("Type");
    	tableColumnRight.setEditable(true);
    	
    	
    	TableView tableView = new TableView();
    	tableView.setPadding(new Insets(0, 0, 0, 0));
    	tableView.getColumns().addAll(tableColumnLeft, tableColumnRight);
    	
    	AnchorPane anchorPane = new AnchorPane(tableView);
    	anchorPane.setPadding(new Insets(0, 0, 0, 0));
    	anchorPane.setScaleShape(true);
    	anchorPane.setCacheShape(true);
    	anchorPane.setCenterShape(true);
    	
    	anchorPane.setTopAnchor(tableView, 0.0);
    	anchorPane.setRightAnchor(tableView, 0.0);
    	anchorPane.setLeftAnchor(tableView, 0.0);
    	anchorPane.setBottomAnchor(tableView, 0.0);

    	tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    	
    	tab.setContent(anchorPane);
    	tabPane.setPadding(new Insets(0, 0, 0, 0));
        tabPane.getTabs().add(tab);
    }
}