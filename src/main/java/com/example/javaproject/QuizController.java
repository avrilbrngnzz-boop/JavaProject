package com.example.javaproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.util.List;
import java.util.ArrayList;

public class QuizController {
    @FXML private Label scoreLabel;
    @FXML private Label questionLabel;
    @FXML private Label vraiFauxLabel;
    @FXML private HBox reponseBox;
    @FXML private Button suivantButton;

    private int score = 0;
    private String correctReponse = "";

    @FXML public void initialize(){
        suivantButton.setVisible(false);
        vraiFauxLabel.setText("");
        scoreLabel.setText("0/5");
        questionLabel.setText("Chargement...");

        //TEST
        ArrayList<String> reponsesTest = new ArrayList<>();
        reponsesTest.add("Paris");
        reponsesTest.add("Londres");
        reponsesTest.add("Berlin");
        reponsesTest.add("Madrid");
        setQuestion("Quelle est la capitale de la France ?", "Paris", reponsesTest);
    }

    public void setQuestion(String question, String correct, List<String> answers ){
        correctReponse = correct;
        vraiFauxLabel.setText("");
        suivantButton.setVisible(false);
        reponseBox.getChildren().clear();
        questionLabel.setText(question);
        for (String answer : answers) {
            Button btn = new Button(answer);
            btn.setStyle("-fx-background-color: #3a3a5c; -fx-text-fill: white; -fx-font-size: 13px;");
            btn.setOnAction(e -> checkAnswer(answer));
            reponseBox.getChildren().add(btn);
        }
    }

    private void checkAnswer(String chosen) {
        for (int i = 0; i < reponseBox.getChildren().size(); i++){
            reponseBox.getChildren().get(i).setDisable(true);
        }
        if (chosen.equals(correctReponse)) {
            score += 1;
            scoreLabel.setText(score + "/5");
            vraiFauxLabel.setText("Correct !");
            vraiFauxLabel.setStyle("-fx-text-fill: green;");
        } else{
            vraiFauxLabel.setText("Mauvaise réponse ! C'etait : " + correctReponse);
            vraiFauxLabel.setStyle("-fx-text-fill: red;");
        }
        suivantButton.setVisible(true);
    }

    @FXML
    private void onNext() {
        if (score >= 5) {
            System.out.println("Bravo tu as eu 5 bonnes réponses !!");
        } else {
            questionLabel.setText("Chargement...");
            reponseBox.getChildren().clear();
            vraiFauxLabel.setText("");
            suivantButton.setVisible(false);
        }
    }
}
