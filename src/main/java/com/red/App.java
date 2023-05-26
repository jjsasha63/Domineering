package com.red;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class App extends Application {

    Board board;
    private GridPane gridPane;

    Integer hUi,vUi, start;

    private Label turnField;

    private Boolean single = false, cp = false;

    public void start(Stage primaryStage) {
        // Create the UI elements for the first window
        TextField hField = new TextField();
        TextField vField = new TextField();
        Button startButton = new Button("Start");
        startButton.setDisable(true);


        ToggleGroup modeToggleGroup = new ToggleGroup();
        RadioButton singlePlayerRadioButton = new RadioButton("Single Player");
        RadioButton multiplayerRadioButton = new RadioButton("Multiplayer");
        singlePlayerRadioButton.setToggleGroup(modeToggleGroup);
        multiplayerRadioButton.setToggleGroup(modeToggleGroup);

        ToggleGroup firstPlayerToggleGroup = new ToggleGroup();
        RadioButton player1RadioButton = new RadioButton("Player");
        RadioButton player2RadioButton = new RadioButton("PC");
        player1RadioButton.setToggleGroup(firstPlayerToggleGroup);
        player2RadioButton.setToggleGroup(firstPlayerToggleGroup);
        player1RadioButton.setSelected(true);

        ToggleGroup sideToggleGroup = new ToggleGroup();
        RadioButton horizontalRadioButton = new RadioButton("Horizontal");
        RadioButton verticalRadioButton = new RadioButton("Vertical");
        horizontalRadioButton.setToggleGroup(sideToggleGroup);
        verticalRadioButton.setToggleGroup(sideToggleGroup);
        horizontalRadioButton.setSelected(true);


        modeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (multiplayerRadioButton.isSelected()) {
                player1RadioButton.setDisable(true);
                player2RadioButton.setDisable(true);
            } else {
                player1RadioButton.setDisable(false);
                player2RadioButton.setDisable(false);
            }
        });

        modeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (modeToggleGroup.getSelectedToggle() != null) {
                startButton.setDisable(false); // Enable the start button
            } else {
                startButton.setDisable(true); // Disable the start button
            }
        });

        // Create the grid pane for the first window and set its alignment
        GridPane inputGridPane = new GridPane();
        inputGridPane.setAlignment(Pos.CENTER);
        inputGridPane.setPadding(new Insets(10));
        inputGridPane.setHgap(10);
        inputGridPane.setVgap(10);

        // Add the UI elements to the grid pane
        inputGridPane.addRow(0, new Label("Enter board dimensions:"));
        inputGridPane.addRow(1, new Label("Width (h):"), hField);
        inputGridPane.addRow(2, new Label("Height (v):"), vField);
        inputGridPane.addRow(3, new Label("Game Mode:"), singlePlayerRadioButton, multiplayerRadioButton);
        inputGridPane.addRow(4, new Label("First Player:"), player1RadioButton, player2RadioButton);
        inputGridPane.addRow(5, new Label("Side:"), horizontalRadioButton, verticalRadioButton);
        inputGridPane.add(startButton, 1, 6);

        // Create the scene for the first window
        Scene inputScene = new Scene(inputGridPane, 500, 250);

        // Create the grid pane for the second window
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // Create the rectangles representing the board cells

        // Create the TextFields for displaying the turn
        turnField = new Label();
//        turnField.setEditable(false);
        turnField.setAlignment(Pos.CENTER);
        turnField.setPrefWidth(200);

        // Create the back button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            hField.clear();
            vField.clear();
            // Remove the rectangles from the grid pane
            gridPane.getChildren().clear();
            primaryStage.setScene(inputScene);
            primaryStage.setTitle("Enter Board Dimensions");
        });

        // Create the VBox for the second window
        VBox gridVBox = new VBox(10);
        gridVBox.setAlignment(Pos.CENTER);
        gridVBox.getChildren().addAll(turnField, gridPane, backButton);

        // Create the scene for the second window
        Scene gridScene = new Scene(gridVBox);

        // Set up the event handler for the start button
        startButton.setOnAction(event -> {
            int ho = Integer.parseInt(hField.getText());
            int ve = Integer.parseInt(vField.getText());
            board = new Board(ho,ve);
            try {
                init(board.getHorizontal(),board.getVertical());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            single = singlePlayerRadioButton.isSelected();
            cp = player2RadioButton.isSelected();
            if(verticalRadioButton.isSelected()){
                board.setState(2);
                start = 2;
            }



            Rectangle[][] rectangles = new Rectangle[board.getVertical()][board.getHorizontal()];
            for (int i = 0; i < board.getVertical(); i++) {
                for (int j = 0; j < board.getHorizontal(); j++) {
                    rectangles[i][j] = createRectangle(i, j);
                    gridPane.add(rectangles[i][j], j, i);
                }
            }
            // Reset the turn field
            turnField.setText("");
            primaryStage.setScene(gridScene);
            primaryStage.setTitle("Dominini Game");
        });

        // Set the initial scene to the input scene
        primaryStage.setScene(inputScene);
        primaryStage.setTitle("Enter Board Dimensions");
        primaryStage.show();
    }




    private Rectangle createRectangle(int row, int col) {
        Rectangle rectangle = new Rectangle(400/hUi,400/vUi);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);

        if(board.getState()!=3){
        rectangle.setOnMouseClicked(event -> {
            try {
                handleCellClick(row, col);
            } catch (InterruptedException | CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });}

        return rectangle;
    }

    private void handleCellClick(int row, int col) throws InterruptedException, CloneNotSupportedException {

        int[] move = new int[2];
        if(single){
            if(cp){
                if(board.getState()==start) {
                    move = CP1.nextMove(board);
                    if(board.getState()==2)board.vertical(move[0],move[1]);
                    else board.horizontal(move[0],move[1]);
                } else {
                    if(board.getState()==2)board.vertical(col,row);
                    else board.horizontal(col,row);
                }
            } else {
                if(board.getState()==start) {
                    if(board.getState()==2)board.vertical(col,row);
                    else board.horizontal(col,row);
                } else {
                    move = CP1.nextMove(board);
                    if(board.getState()==2)board.vertical(move[0],move[1]);
                    else board.horizontal(move[0],move[1]);
                }
            }

        } else {
            if (board.getState()==1) {
                board.horizontal(col, row);
            } else if (board.getState()==2) {
                board.vertical(col, row);
            }
        }

        updateUI();
    }

    private void updateUI() {
        turnField.setText(board.getMes());
        for (int i = 0; i < vUi; i++) {
            for (int j = 0; j < hUi; j++) {
                Rectangle rectangle = (Rectangle) gridPane.getChildren().get(i * hUi + j);
                if (board.getBoard()[i][j] == 0) {
                    rectangle.setFill(Color.WHITE);
                } else if (board.getBoard()[i][j] == 1) {
                    rectangle.setFill(Color.RED);
                } else if (board.getBoard()[i][j] == 2) {
                    rectangle.setFill(Color.BLUE);
                }
            }
        }
    }



    public void init(int h, int v){
        board.setState(1);
        start = 1;
//        board.setR(0);
//        board.setC(0);
        hUi = h;
        vUi = v;
//        board.setBoard(new Integer[v][h]);
//        for (int j = board.getR(); j < v; j++) {
//            for (int k = board.getC(); k < h; k++) {
//                board.getBoard()[j][k] = 0;
//            }
//        }
    }

}
