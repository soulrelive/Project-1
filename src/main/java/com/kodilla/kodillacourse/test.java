package com.kodilla.kodillacourse;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.*;
import java.util.stream.Collectors;


public class test extends Application {

    GridPane gridPane = new GridPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        Group root = new Group();
        Scene scene = new Scene(root, 605, 605, Color.BLACK);

        gridPane.setPrefSize(600, 600);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(15, 15, 15, 15));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setPrefSize(200, 200);
                button.setFont(new Font(50));
                gridPane.add(button, i, j);

                button.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        button.setText("X");
                        isWon(gridPane, "X");
                        computerTurn(gridPane);
                    }
                });
            }
        }

        root.getChildren().addAll(gridPane);
        primaryStage.setTitle("Test");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void computerTurn(GridPane grid) {
        ObservableList<Node> children = grid.getChildren();
        List<Node> buttons = children.stream().filter(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                if (button.getText().equals("X") || (button.getText().equals("0"))) {
                    return false;
                } else {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        markComputerTurn(buttons);
        isWon(grid, "0");

    }

    private void markComputerTurn(List<Node> buttons) {
        Random r = new Random();
        int chosenButtonNumber = r.nextInt(buttons.size());
        Button chosen = (Button) buttons.get(chosenButtonNumber);
        chosen.setText("0");
    }

    private void isWon(GridPane grid1, String symbol) {
        ObservableList<Node> children = grid1.getChildren();
        List<Node> buttons1 = children.stream().filter(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                if (symbol.equals(button.getText())) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }).collect(Collectors.toList());
        if (buttons1.size() > 2) {
            checkPositions(buttons1, symbol);
        }
    }

    public void checkPositions(List<Node> nodes, String symbol) {
        List<Position> positions = nodes.stream().map(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer columnIndex = GridPane.getColumnIndex(node);
            return new Position(rowIndex, columnIndex, symbol);
        }).collect(Collectors.toList());
        if (checkRows(positions) || checkColumns(positions) || checkLeftSlash(positions, symbol) || checkRightFlash(positions, symbol)) {
            showDialog(symbol);
        }
    }

    public boolean checkRows(List<Position> positionList) {
        Map<Integer, Integer> rows = new HashMap<>();
        for (Position position : positionList) {
            if (rows.containsKey(position.getRow())) {
                int value = rows.get(position.getRow());
                rows.put(position.getRow(), ++value);
            } else {
                rows.put(position.getRow(), 1);
            }
        }
        boolean b = rows.values().stream().anyMatch(i -> i == 3);
        return b;
    }

    public boolean checkColumns(List<Position> positionList) {
        Map<Integer, Integer> columns = new HashMap<>();
        for (Position position : positionList) {
            if (columns.containsKey(position.getColumn())) {
                int value1 = columns.get(position.getColumn());
                columns.put(position.getColumn(), ++value1);
            } else {
                columns.put(position.getColumn(), 1);
            }
        }
        boolean c = columns.values().stream().anyMatch(i -> i == 3);
        return c;
    }

    public boolean checkLeftSlash(List<Position> positionList, String symbol) {
        List<Position> collect = positionList.stream().filter(position -> {
            if (position.getRow() == 0 && (position.getColumn() == 0)) {
                return true;
            }
            if (position.getRow() == 1 && (position.getColumn() == 1)) {
                return true;
            }
            if (position.getRow() == 2 && (position.getColumn() == 2)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (collect.size() != 3) {
            return false;
        } else {
            return collect.stream().allMatch(c -> c.getSymbol().equals(symbol));
        }
    }

    public boolean checkRightFlash(List<Position> positionList, String symbol) {
        List<Position> collect1 = positionList.stream().filter(position -> {
            if (position.getRow() == 0 && (position.getColumn() == 2)) {
                return true;
            }
            if (position.getRow() == 1 && (position.getColumn() == 1)) {
                return true;
            }
            if (position.getRow() == 2 && (position.getColumn() == 0)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (collect1.size() != 3) {
            return false;
        } else {
            return collect1.stream().allMatch(c -> c.getSymbol().equals(symbol));
        }
    }

    public void showDialog(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(name + " Won");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent() && (buttonType.get() == ButtonType.CANCEL)) {
            System.exit(0);
        }
        if (buttonType.isPresent() && (buttonType.get() == ButtonType.OK)) {
            restartGame();
        }
    }

    public void restartGame() {
        gridPane.getChildren().stream().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setText("");
            }
        });
    }
}

