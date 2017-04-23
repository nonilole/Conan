package view;

import java.util.*;
import javafx.application.Platform;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import model.BoxReference;
import model.Proof;
import model.ProofListener;
import view.Command.*;


/***
 * The ProofView panes consists of two VBoxes
 *
 * One for lineNumbers and one for the rows with TextFields.
 *
 * LineNo | rows
 * -------------------
 * 1      | BorderPane
 * 2      | BorderPane
 * 3      | BorderPane
 * 4      | BorderPane
 *
 * The row is a BorderPane and consists of one TextField and a RulePane
 *
 * Row
 * =============
 * Center - a TextField for the expression
 * Right - a RulePane that consists of four textfields for the rule and rule prompts
 *
 * Center can be reached by calling the BorderPanes method's getExpression().
 * Remember to cast to TextField.
 * E.g. (TextField) rList.get(rList.size()-1).getExpression()
 *
 * Right - a RulePane the consists of four Textfields for the rule and rule prompts
 * The four textfields to the right can be reached by calling getRule(), getRulePrompt1(), 2 and 3.
 *
 * Keep in mind, that each child of rows is not a BorderPane. Boxes are additional VBoxes, with styling.
 * E.g.
 * rows
 * ====
 * Box
 *      BorderPane
 *      BorderPane
 *      Box
 *          BorderPane
 *      BorderPane
 * BorderPane
 */
public class ProofView extends Symbolic implements ProofListener, View {
	/*
	 * These are magic constants that decide the lineNo padding.
	 * Margin can't be changed as a property, so the solution is to take into account how much the border
	 * and the padding increases the distance between rows and add the padding to the line numbers accordingly.
	 */
	static final int carryAddOpen = 3;
	static final int carryAddClose = 5;


	// TextFields of the premises and conclusion for quick access
	private TextField premises;
	private TextField conclusion;

	private Stack<VBox> curBoxDepth = new Stack<>();

	// This is a list of RowPanes, which are the "lines" of the proof.
	private List<RowPane> rList = new ArrayList<>();
	//private int counter = 1;
	//private int carry = 0;
	private List<Command> commandList = new LinkedList<Command>();
	private int curCommand = -1;

	private VBox lineNo;
	private VBox rows;

	//The tab object of this view
	private ViewTab tab;

	//The proof displayed in this view
	private Proof proof;

	//The patth where this proof was loaded/should be saved
	private String path;

	//Name of the proof/file of this view
	private String name;

	//hashmap for all the rules and number of arguments for all rules
	private HashMap<String, Integer> ruleMap = new HashMap<String, Integer>();


	// Key combinations
	final static KeyCombination shiftEnter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_DOWN);
    final static KeyCombination ctrlB = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);
	final static KeyCombination ctrlD = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
	// Pattern for prompt
	// Match at least one digit perhaps one dash and then any number of digits. Or match nothing at all.
	static Pattern p = Pattern.compile("^(\\d+-?\\d*)?$");

	ScrollPane sp;

	private AnchorPane createProofPane() {
		lineNo = new VBox();
		rows = new VBox();
		lineNo.setFillWidth(true);
		rows.setFillWidth(true);
		HBox hb = new HBox();
		hb.setHgrow(rows, Priority.ALWAYS);
		hb.getChildren().addAll(lineNo, rows);
		hb.setPadding(new Insets(5, 5, 5, 5));
		ScrollPane sp = new ScrollPane(hb);
		sp.getStyleClass().add("fit");
//		hb.heightProperty().addListener((ov, oldValue, newValue) -> {
//			if (newValue.doubleValue() > oldValue.doubleValue()) { // Change this to only trigger on new row!!
//				sp.setVvalue(1.0);                                 // Otherwise it will scroll down when you insert a row in the middle
//			}
//		});
		AnchorPane proofPane = new AnchorPane(sp);
		proofPane.setTopAnchor(sp, 0.0);
		proofPane.setRightAnchor(sp, 0.0);
		proofPane.setLeftAnchor(sp, 0.0);
		proofPane.setBottomAnchor(sp, 0.0);

		return proofPane;
	}


	/**
	 * Adds content to the TabPane in the proof and adds listeners to the premise and conclusion.
	 * Vad är det som metoden gör med proof? lägg gärna till extra beskrivning om detta!!
	 * @param tabPane
	 * @param premisesAndConclusion
	 */

	public ProofView(TabPane tabPane, Proof proof, PremisesAndConclusion premisesAndConclusion) {
		this.proof = proof;
		this.proof.registerProofListener(this);
		this.premises = premisesAndConclusion.getPremises();
		this.premises.setId("expression");
		this.premises.setPromptText("Premise");
		premises.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				premises.setText(checkShortcut(newValue));
			}
		});
		this.conclusion = premisesAndConclusion.getConclusion();
		this.conclusion.setId("expression");
		this.conclusion.setPromptText("Conclusion");
		conclusion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				conclusion.setText(checkShortcut(newValue));
			}
		});
		this.conclusion.textProperty().addListener((ov, oldValue, newValue) -> {
			proof.updateConclusion(newValue);
		});
		proof.updateConclusion(this.conclusion.getText());

		this.premises.focusedProperty().addListener((observable, oldValue, newValue) -> {
			lastFocusedTf = this.premises;
			caretPosition = this.premises.getCaretPosition();
		});
		this.conclusion.focusedProperty().addListener((observable, oldValue, newValue) -> {
			lastFocusedTf = this.conclusion;
			caretPosition = this.conclusion.getCaretPosition();
		});
		AnchorPane cp=createProofPane();
		SplitPane sp = new SplitPane(premisesAndConclusion, cp);
		sp.setOrientation(Orientation.VERTICAL);
		sp.setDividerPosition(0, 0.1);
		AnchorPane anchorPane = new AnchorPane(sp);
		anchorPane.setTopAnchor(sp, 0.0);
		anchorPane.setRightAnchor(sp, 0.0);
		anchorPane.setLeftAnchor(sp, 0.0);
		anchorPane.setBottomAnchor(sp, 0.0);

		this.tab = new ViewTab("Proof", this);
		this.tab.setContent(anchorPane);
		tabPane.getTabs().add(this.tab);
		tabPane.getSelectionModel().select(this.tab); // Byt till den nya tabben
		newRow();
        --curCommand;
		commandList.clear();
		initializeRuleMap();
	}

	/**
	 * a method to initialize the ruleMap
	 * TODO: keep track of when it is a interval
	 */
	private void initializeRuleMap() {
		ruleMap.put("∧I", 2);
		ruleMap.put("∧E1", 1);
		ruleMap.put("∧E2", 1);
		ruleMap.put("∨I1", 1);
		ruleMap.put("∨I2", 1);
		ruleMap.put("∨E", 3);
		ruleMap.put("→I", 1);
		ruleMap.put("→E", 2);
		ruleMap.put("⊥E", 1);
		ruleMap.put("¬I", 1);
		ruleMap.put("¬E", 2);
		ruleMap.put("¬¬E", 1);
		ruleMap.put("Premise", 0);
		ruleMap.put("∀I", 1);
		ruleMap.put("∀E", 1);
		ruleMap.put("∃I", 1);
		ruleMap.put("∃E", 2);
		ruleMap.put("=E", 2);
		ruleMap.put("=I", 0);
		ruleMap.put("Ass.", 0);
		ruleMap.put("Fresh", 0);
		ruleMap.put("MT", 2);
		ruleMap.put("LEM", 0);
	}


	public ProofView(TabPane tabPane, Proof proof) {
		this(tabPane, proof, new PremisesAndConclusion());
	}
	public ProofView(TabPane tabPane, Proof proof, String sPremises, String sConclusion) {
		this(tabPane, proof, new PremisesAndConclusion(sPremises, sConclusion));
	}
	/* Controller begin */
	public void openBox() {
		proof.openBox();
	}
	public void closeBox() {
		proof.closeBox();
	}
	private void executeCommand(Command c) {
	    if (c.execute()) {
            ++curCommand;
	        if (0 <= curCommand && curCommand < commandList.size())
                commandList.subList(curCommand, commandList.size()).clear();
			commandList.add(c);
		}
    }
    public void newRow() {
        executeCommand(new AddRow(proof, rList));
    }
	public void addRowAfterBox(int rowNo){
		// Let's assume this is possible and check if it's the last row in a box or else we execute insertNewRow
		RowPane rp = rList.get(rowNo-1);
		VBox parent = (VBox) rp.getParent();
		int idxOfRp = parent.getChildren().indexOf(rp);
		if (parent.getParent() instanceof VBox && idxOfRp == parent.getChildren().size()-1)
            executeCommand(new AddRowAfterBox(proof, rowNo, rList));
		else
            executeCommand(new InsertRow(proof, rowNo, BoxReference.AFTER, rList));

	}
	public void insertNewRow(int rowNo, BoxReference br){
		executeCommand(new InsertRow(proof, rowNo, br, rList));
	}
	public void insertNewBox(int rowNo){ executeCommand(new InsertBox(proof, rowNo, rList));}
	public void deleteRow(int rowNo){
		executeCommand(new DeleteRow(proof, rowNo, rList));
	}
	public void undo() {
	    if (0 <= curCommand && curCommand < commandList.size()) {
	        commandList.get(curCommand).undo();
	        --curCommand;
		}
	}
	public void redo() {
		if (0 <= curCommand+1 && curCommand+1 < commandList.size()) {
			commandList.get(curCommand+1).execute();
			++curCommand;
		}
	}
	/* End of controller */

	private void checkAndAdd(Region item) {
		if (curBoxDepth.isEmpty()) {
			rows.getChildren().add(item);
		} else {
			VBox temp = curBoxDepth.peek();
			temp.getChildren().add(item);
		}
	}

	/**
	 * Creates a new row with a textfield for the expression and a textfield
	 * for the rules and adds listeners to both of them.
	 * @return bp, the BorderPane containing two textfields.
	 */
	private RowPane createRow(boolean isFirstRowInBox, int nrOfClosingBoxes) {
		//borderpane which contains the textfield for the expression and the rule
		RowPane bp = new RowPane(isFirstRowInBox,nrOfClosingBoxes);

        //setting up a context menu for right-clicking a textfield
		ContextMenu contextMenu = new ContextMenu();
		MenuItem delete = new MenuItem("Delete");
		MenuItem insertAbove = new MenuItem("Insert Above");
		MenuItem insertHere = new MenuItem("Insert Here");
		MenuItem insertBelow = new MenuItem("Insert Below");
		MenuItem insertBox = new MenuItem("Insert Box");
		contextMenu.getItems().add(delete);
		contextMenu.getItems().add(insertAbove);
		contextMenu.getItems().add(insertHere);
		contextMenu.getItems().add(insertBelow);
		contextMenu.getItems().add(insertBox);
		bp.getRule().setContextMenu(contextMenu);
		for(int i = 0;i<3; i++) {
			bp.getRulePrompt(i).setContextMenu(contextMenu);
		}
		bp.getExpression().setContextMenu(contextMenu);
		bp.getRule().setContextMenu(contextMenu);

		
		delete.setOnAction(event -> {
			int rowOfPressedButton=rList.indexOf(bp) + 1;
			deleteRow(rowOfPressedButton);
		});;
		insertAbove.setOnAction(event -> {
			int rowOfPressedButton=rList.indexOf(bp) + 1;
			insertNewRow(rowOfPressedButton, BoxReference.BEFORE);
		});;
		insertHere.setOnAction(event -> {
			int rowOfPressedButton=rList.indexOf(bp) + 1;
			insertNewRow(rowOfPressedButton, BoxReference.AFTER);
		});;
		insertBelow.setOnAction(event -> {
			int rowOfPressedButton=rList.indexOf(bp) + 1;
			addRowAfterBox(rowOfPressedButton);
		});;
		insertBox.setOnAction(event -> {
			int rowOfPressedButton=rList.indexOf(bp) + 1;
			insertNewBox(rowOfPressedButton);
		});;


		//adding listeners to the expression- and rule textfield
		TextField tfExpression = bp.getExpression();
		tfExpression.setPromptText("Expression");

		tfExpression.focusedProperty().addListener((observable, oldValue, newValue) -> {
			lastFocusedTf = tfExpression;
			caretPosition = tfExpression.getCaretPosition();
		});
		
		TextField tfRule = bp.getRule();
		tfRule.setPromptText("Rule");
		
		tfRule.focusedProperty().addListener((observable, oldValue, newValue) -> {
			lastFocusedTf = tfRule;
			caretPosition = tfRule.getCaretPosition();
		});
		for (int i = 0; i < 3; i++) {
			int finalI = i;
			bp.getRulePrompt(i).setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent ke) {
					int index = rList.indexOf(bp); //Kanske vill flytta in den här för prestanda
					if (ctrlD.match(ke)) {
						deleteRow(index+1);
					} else if (ctrlB.match(ke)) {
                        insertNewBox(index + 1);
                    } else if (shiftEnter.match(ke)) {
						addRowAfterBox(index+1);
						rList.get(index).getClosestPromptFromLeft(finalI).requestFocus();
					} else if (ke.getCode() == KeyCode.ENTER) {
						insertNewRow(index + 1, BoxReference.AFTER);
						rList.get(index + 1).getClosestPromptFromLeft(finalI).requestFocus();
					} else if (ke.getCode() == KeyCode.DOWN) {
						if (index + 1 < rList.size()) {
							rList.get(index + 1).getClosestPromptFromLeft(finalI).requestFocus();
						}
					} else if (ke.getCode() == KeyCode.UP) {
						if (index - 1 >= 0) {
							rList.get(index - 1).getClosestPromptFromLeft(finalI).requestFocus();
						}
					}
				}
			});
		}

		tfExpression.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
                int index = rList.indexOf(bp); //Kanske vill flytta in den här för prestanda
				if (ctrlD.match(ke)) {
					deleteRow(index + 1);
				} else if (ctrlB.match(ke)) {
                    insertNewBox(index+1);
                } else if (shiftEnter.match(ke)) {
                    addRowAfterBox(index+1);
                    rList.get(index+1).getExpression().requestFocus();
				} else if (ke.getCode() == KeyCode.ENTER) {
			        insertNewRow(index+1, BoxReference.AFTER);
					rList.get(index+1).getExpression().requestFocus();
				} else if (ke.getCode() == KeyCode.DOWN) {
					if(index+1<rList.size()) {
						rList.get(index+1).getExpression().requestFocus();
					}
				} else if(ke.getCode() == KeyCode.UP) {
                    if (index - 1 >= 0) {
                        rList.get(index - 1).getExpression().requestFocus();
                    }
                }
			}
		});
		tfRule.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				int index = rList.indexOf(bp);
				if (ctrlD.match(ke)) {
					deleteRow(index + 1);
				} if (ctrlB.match(ke)) {
                    insertNewBox(index + 1);
                } else if (shiftEnter.match(ke)) {
					addRowAfterBox(index+1);
					rList.get(index).getRule().requestFocus();
				} else if (ke.getCode() == KeyCode.ENTER) {
					insertNewRow(index+1, BoxReference.AFTER);
					rList.get(index+1).getRule().requestFocus();
				} else if (ke.getCode() == KeyCode.DOWN) {
					if(index+1<rList.size()) {
						rList.get(index+1).getRule().requestFocus();
					}
				} else if(ke.getCode() == KeyCode.UP) {
					if(index-1>=0) {
						rList.get(index-1).getRule().requestFocus();
					}
				}
			}
		});
		tfRule.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				tfRule.setText(checkShortcut(newValue));
			}
		});
		tfExpression.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				tfExpression.setText(checkShortcut(newValue));
			}
		});
		return bp;
	}


	//should only be called AFTER a new row has been added to rList since it uses rList.size()
	Label createLabel() {
		Label lbl = new Label(""+rList.size());
		lbl.getStyleClass().add("lineNo");
		lbl.setPadding(new Insets(8,2,2,2));
		//lbl.setCache(true);
		//lbl.setCacheShape(true);
		lbl.setCacheHint(CacheHint.SPEED);
		return lbl;
	}


	// PROOF LISTENER METHODS
	//Adds a new row at the end of the proof
	public void rowAdded(){
		RowPane rp;
		if(curBoxDepth.isEmpty()){
			rp = createRow(false, 0);
			rList.add(rp);
			rows.getChildren().add(rp);
		} else {
			VBox box = curBoxDepth.peek();
			List<Node> children = box.getChildren();
			boolean isFirstRowInBox = (children.isEmpty()) ? true : false;
			rp = createRow(isFirstRowInBox, 0);
			rList.add(rp);
			children.add(rp);
		}
		lineNo.getChildren().add(createLabel());
		updateLabelPaddings(rList.size());
		addListeners(rp);
	}
	//rowNo is which row the reference row is at, BoxReference tells you if you want to add the new row before or after
	public void rowInserted(int rowNo, BoxReference br) {
		RowPane referenceRow;//row we'll use to get a refence to the box where we add the new row
		boolean isFirstRowInBox;
		int nrOfClosingBoxes; //the nr of boxes that closes at line rowNo
		int indexToInsertInParent;
		VBox parentBox; // which vbox to display the new row in
		int rListInsertionIndex = (br == BoxReference.BEFORE) ? rowNo-1 : rowNo;

		if(br == BoxReference.BEFORE){
			referenceRow = rList.get(rowNo-1);
			isFirstRowInBox = referenceRow.getIsFirstRowInBox();
			nrOfClosingBoxes = 0;
			referenceRow.setIsFirstRowInBox(false);
			parentBox = (VBox)referenceRow.getParent();
			indexToInsertInParent = parentBox.getChildren().indexOf(referenceRow);

		}
		else{ //br == BoxReference.AFTER
			referenceRow = rList.get(rowNo-1);
			nrOfClosingBoxes = referenceRow.getNrOfClosingBoxes();
			isFirstRowInBox = false;
			referenceRow.setNrOfClosingBoxes(0);
			parentBox = (VBox)referenceRow.getParent();
			indexToInsertInParent = parentBox.getChildren().indexOf(referenceRow) + 1;
		}
		RowPane rp = createRow(isFirstRowInBox, nrOfClosingBoxes);
		// ((TextField)rp.getExpression()).setText("*");
		parentBox.getChildren().add(indexToInsertInParent,rp);
		rList.add(rListInsertionIndex, rp);
		lineNo.getChildren().add(createLabel());
		updateLabelPaddings(rowNo);
		addListeners(rp);
	}
	public void addedRowAfterBox(int rowNo) {
		RowPane referenceRow;
		referenceRow = rList.get(rowNo-1); //Assume this is the last row in box
		VBox metaBox = (VBox) referenceRow.getParent().getParent();
		int nrOfClosingBoxes = referenceRow.getNrOfClosingBoxes();
		referenceRow.setNrOfClosingBoxes(1);
		RowPane rp = createRow(false, nrOfClosingBoxes-1);
		int idx = metaBox.getChildren().indexOf(referenceRow.getParent()) + 1;
		metaBox.getChildren().add(idx, rp);
		rList.add(rowNo, rp);
		lineNo.getChildren().add(createLabel());
		updateLabelPaddings(rowNo);
		addListeners(rp);
	}
	public void deletedRowAfterBox(int rowNo) {
		RowPane referenceRow;
		referenceRow = rList.get(rowNo-1); //Assume this is the last row in box
		VBox metaBox = (VBox) referenceRow.getParent().getParent();
		int nrOfClosingBoxes = referenceRow.getNrOfClosingBoxes();
		int extraClosedBoxes = rList.get(rowNo).getNrOfClosingBoxes();
		referenceRow.setNrOfClosingBoxes(nrOfClosingBoxes+extraClosedBoxes);
		int idx = metaBox.getChildren().indexOf(referenceRow.getParent()) + 1;
		metaBox.getChildren().remove(idx);
		rList.remove(rowNo);
		List<Node> labelList = lineNo.getChildren();
		labelList.remove(labelList.size()-1);
		updateLabelPaddings(rowNo-1);
	}
	public void boxOpened(){
		VBox vb = new VBox();
		vb.getStyleClass().add("openBox");
		checkAndAdd(vb);
		curBoxDepth.push(vb);
		newRow();
	}
	public void boxInserted(int rowNumber){
		RowPane rp = rList.get(rowNumber-1);
		VBox metabox = (VBox) rp.getParent();
		int indexOfRp = metabox.getChildren().indexOf(rp);
		VBox newBox = new VBox();
		newBox.getStyleClass().clear();
		newBox.getStyleClass().add("closedBox");
		newBox.getChildren().add(rp);
		metabox.getChildren().add(indexOfRp, newBox);
		rp.setIsFirstRowInBox(true);
		rp.incrementNrOfClosingBoxes();
		updateLabelPaddings(rowNumber);
	}
	public void boxRemoved(int rowNumber) {
		RowPane rp = rList.get(rowNumber-1);
		rp.decrementNrOfClosingBoxes();
		VBox metabox = (VBox) rp.getParent().getParent();

		ObservableList<Node> children = metabox.getChildren();
		int idx = children.indexOf(rp.getParent());
		children.remove(idx);
		children.add(idx, rp);
		if (idx != 0) {
			rp.setIsFirstRowInBox(false);
		}
		updateLabelPaddings(rowNumber);
	}
	public void boxClosed(){
		if (!curBoxDepth.isEmpty()) {
			VBox vb = curBoxDepth.pop();

			//Update last row and its padding
			rList.get(rList.size()-1).incrementNrOfClosingBoxes();
			updateLabelPaddings(rList.size()-1);

			vb.getStyleClass().clear();
			vb.getStyleClass().add("closedBox");

		}
	}
	public void rowUpdated(boolean wellFormed, int lineNo) {
		TextField expression = (TextField) rList.get(lineNo-1).getExpression();
		if (expression.getText().equals(""))
			wellFormed = true;
		applyStyleIf(expression, !wellFormed, "bad");
	}
	public void conclusionReached(boolean correct, int lineNo){
		TextField expression = (TextField) rList.get(lineNo-1).getExpression();
		applyStyleIf(expression, correct, "conclusionReached");
	}
	public void rowVerified(boolean verified, int lineNo) {
		TextField rule = (TextField) rList.get(lineNo-1).getRule();
		applyStyleIf(rule, !verified, "unVerified");
	}
	//update view to reflect that row with nr rowNr has been deleted
	public void rowDeleted(int rowNr){
		RowPane rp = rList.get(rowNr-1);
		VBox box = (VBox)rp.getParent();
		List<Node> parentComponentList = box.getChildren();
		if(parentComponentList.remove(rp) == false){
			System.out.println("ProofView.rowDeleted: something went wrong!");
			return;
		}
		rList.remove(rp);
		boolean wasOnlyRowInBox = parentComponentList.isEmpty();
		boolean updatePreviousRowLabel = false;

		//deleted row was last row in this box, remove the box
		if(wasOnlyRowInBox){
			removeRecursivelyIfEmpty(box);
		}
		else{
			if(rp.getIsFirstRowInBox()){ // next row is now first in this box
				RowPane nextRow = rList.get(rowNr-1);
				nextRow.setIsFirstRowInBox(rp.getIsFirstRowInBox());
			}
			if(rp.getNrOfClosingBoxes() > 0){ // previous row now closes the boxes
				rList.get(rowNr-2).setNrOfClosingBoxes(rp.getNrOfClosingBoxes());
				updatePreviousRowLabel = true;
			}
		}

		//remove a Label and update paddings in relevant labels
		List<Node> labelList = lineNo.getChildren();
		labelList.remove(labelList.size()-1);
		updateLabelPaddings( updatePreviousRowLabel ? rowNr-1 : rowNr  );
		int idxToFocus = rowNr-1;
		if (idxToFocus >= rList.size()) {
			idxToFocus = rList.size()-1;
		}
		RowPane focusThisPane = rList.get(idxToFocus);
		focusThisPane.getExpression().requestFocus();
	}
	// END OF PROOF LISTENER METHODS




	// public void focus() { // Save the last focused textfield here for quick resuming?
	//     Platform.runLater(() -> lastTf.requestFocus());
	// }
	private void applyStyleIf(TextField expression, boolean bool, String style) {
		expression.getStyleClass().removeIf((s) -> s.equals(style));
		if (bool) {
			expression.getStyleClass().add(style);
		}
	}
	public ViewTab getTab(){ return tab;}
	public Proof getProof(){ return proof;}
	public String getPath(){ return path;}
	public void setPath(String path){ this.path = path; }
	public String getName(){ return name;}
	public void setName(String name){ this.name = name; }

	//updates the padding for the labels starting from lineNr all the way down
	//TODO: get rid of magic numbers
	private void updateLabelPaddings(int lineNr){
		List<Node> labelList = this.lineNo.getChildren();
		assert(rList.size() == labelList.size());
		//update the padding for each label by checking the corresponding rowPane
		for(int i = lineNr-1; i < labelList.size() ; i++){
			RowPane rp = rList.get(i);
			int topPadding = rp.getIsFirstRowInBox() ? 11 : 8;
			int bottomPadding = 2 + 5 * rp.getNrOfClosingBoxes();
			Label label = (Label) labelList.get(i);
			label.setPadding(new Insets(topPadding,2,bottomPadding,2));
		}
	}

	//add listeners for the formula and rule textfields in the RowPane at the given rowNr
	private void addListeners(RowPane rp){

		// Updates the Proof object if the textField is updated
		TextField formulaField = rp.getExpression();
		TextField ruleField = rp.getRule();


		formulaField.textProperty().addListener((ov, oldValue, newValue) -> {
            int rpIndex = rList.indexOf(rp);
			proof.updateFormulaRow(newValue, rpIndex+1);
		});
		// Updates the Proof object if the textField is updated
		ruleField.textProperty().addListener((ov, oldValue, newValue) -> {
            int rpIndex = rList.indexOf(rp);
            try {
                proof.updateRuleRow(newValue, rpIndex+1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            if (newValue.equals("Ass.") || newValue.equals("Fresh")) {
                insertNewBox(rpIndex+1);
            }
			rp.setPrompts(ruleMap.getOrDefault(newValue,-1));
		});
		for (int i = 0 ; i < 3; i++) {
		    TextField prompt = rp.getRulePrompt(i);
			int finalI = i+1;
			prompt.textProperty().addListener((observable, oldValue, newValue) -> {
				Matcher m = p.matcher(newValue);
				if (m.matches()) {
					int rpNr = rList.indexOf(rp)+1;
                    proof.rulePromptUpdate(rpNr, finalI, newValue);
				} else {
					prompt.setText(oldValue);

				}
			});
		}
	}

	//
	private void removeRecursivelyIfEmpty(VBox box){
		Node parentNode = box.getParent();
		assert( box.getChildren().isEmpty() );
		if(parentNode instanceof VBox ){
			VBox parentBox = (VBox) parentNode;
			parentBox.getChildren().remove(box);
			if(box.getStyleClass().toString().equals("openBox")){
				VBox box2 = curBoxDepth.pop();
				assert(box == box2);
			}
			if( parentBox.getChildren().isEmpty() ) {
				removeRecursivelyIfEmpty(parentBox);
			}
		}
	}

	/**
	 * Adds the rule symbol that has been pressed to the text field for the rule when either "expression" or "righTextfield"
	 * is focused.
	 * @param event
	 */
	public void addRule(javafx.event.ActionEvent event){
		if(lastFocusedTf.getId() == "rightTextfield"){
			int tmpCaretPosition = caretPosition;
			String[] parts = event.toString().split("'");
			lastFocusedTf.setText(parts[1]);
			lastFocusedTf.requestFocus();
			lastFocusedTf.positionCaret(tmpCaretPosition+1);
		}
		else if(lastFocusedTf.getId() == "expression"){
			TextField tmpLastFocusedTf = lastFocusedTf;
			BorderPane borderpane = (BorderPane) lastFocusedTf.getParent();
			RulePane rulepane = (RulePane) borderpane.getRight();
			TextField tf = (TextField) rulepane.getChildren().get(0);
			int tmpCaretPosition = caretPosition;
			String[] parts = event.toString().split("'");
			tf.setText(parts[1]);
			tmpLastFocusedTf.requestFocus();
			tmpLastFocusedTf.positionCaret(tmpCaretPosition);
		}
	}

	/**Returns the row index of the last focused textfield in the proof.
	 *@return row index of the last focused textfield in the proof, otherwise it returns -1
	 * */
	public int getRowIndexLastFocusedTF(){
		    if(lastFocusedTf!=null&&rList.indexOf(lastFocusedTf.getParent())!=-1){
		    	return rList.indexOf(lastFocusedTf.getParent())+1;
		    }
		        return -1;
	}
	
	public String checkShortcut(String newValue){
		newValue = newValue.replaceAll("!|ne|no", "¬");
		newValue = newValue.replaceAll("&|an", "∧");
		newValue = newValue.replaceAll("->|im", "→");
		newValue = newValue.replaceAll("fa|fo", "∀");
		newValue = newValue.replaceAll("or", "∨");
		newValue = newValue.replaceAll("ex|te|th", "∃");
		newValue = newValue.replaceAll("co|bo", "⊥");
		return newValue;
	}

}