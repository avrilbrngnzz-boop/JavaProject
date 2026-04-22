package com.example.javaproject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizController {

    @FXML private Label scoreLabel;
    @FXML private Label questionLabel;
    @FXML private Label vraiFauxLabel;
    @FXML private HBox reponseBox;
    @FXML private Button suivantButton;

    private int score = 0;
    private String correctReponse = "";
    private final Gson gson = new Gson();

    @FXML
    public void initialize() {
        suivantButton.setVisible(false);
        vraiFauxLabel.setText("");
        scoreLabel.setText("0/5");

        //TEST
        /*ArrayList<String> reponsesTest = new ArrayList<>();
        reponsesTest.add("Paris");
        reponsesTest.add("Londres");
        reponsesTest.add("Berlin");
        reponsesTest.add("Madrid");
        setQuestion("Quelle est la capitale de la France ?", "Paris", reponsesTest);*/

        chargerNouvelleQuestion();
    }

    //Version Anglaise
    /*private void chargerNouvelleQuestion() {
        questionLabel.setText("Chargement...");
        reponseBox.getChildren().clear();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://opentdb.com/api.php?amount=1&type=multiple"))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(this::traiterResultatAPI)
                    .exceptionally(e -> {
                        Platform.runLater(() -> questionLabel.setText("Erreur réseau, réessayez..."));
                        return null;
                    });
        }
    }

    private void traiterResultatAPI(String json) {
        QuizQuestion data = gson.fromJson(json, QuizQuestion.class);
        if (data != null && data.results != null && !data.results.isEmpty()) {
            QuizQuestion.Result res = data.results.get(0);

            String questionNettoyee = nettoyerTexte(res.question);
            correctReponse = nettoyerTexte(res.correct_answer);

            List<String> toutesReponses = new ArrayList<>();
            toutesReponses.add(correctReponse);
            for(String s : res.incorrect_answers) {
                toutesReponses.add(nettoyerTexte(s));
            }
            Collections.shuffle(toutesReponses);

            // mise à jour de l'interface
            Platform.runLater(() -> {
                questionLabel.setText(questionNettoyee);
                vraiFauxLabel.setText("");
                suivantButton.setVisible(false);

                for (String answer : toutesReponses) {
                    Button btn = new Button(answer);
                    btn.setStyle("-fx-background-color: #3a3a5c; -fx-text-fill: white; -fx-font-size: 13px;");
                    btn.setOnAction(e -> checkAnswer(answer));
                    reponseBox.getChildren().add(btn);
                }
            });
        }
    }*/

    //Version Française
    private void chargerNouvelleQuestion() {
        questionLabel.setText("Chargement...");
        reponseBox.getChildren().clear();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://quizzapi.jomoreschi.fr/api/v2/quiz?limit=1&category=tv_cinema&difficulty=facile"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::traiterResultatAPI)
                .exceptionally(e -> {
                    Platform.runLater(() -> questionLabel.setText("Erreur réseau, réessayez..."));
                    return null;
                });
    }

    private void traiterResultatAPI(String json) {

        QuizFR data = gson.fromJson(json, QuizFR.class);

        if (data != null && data.quizzes != null && !data.quizzes.isEmpty()) {
            QuizFR.Quiz q = data.quizzes.get(0);
            correctReponse = q.answer;
            List<String> toutesReponses = new ArrayList<>();
            toutesReponses.add(q.answer);
            toutesReponses.addAll(q.badAnswers);
            Collections.shuffle(toutesReponses);

            Platform.runLater(() -> {
                questionLabel.setText(q.question);
                vraiFauxLabel.setText("");
                suivantButton.setVisible(false);
                reponseBox.getChildren().clear();

                for (String answer : toutesReponses) {
                    Button btn = new Button(answer);
                    btn.setStyle("-fx-background-color: #3a3a5c; -fx-text-fill: white; -fx-font-size: 13px;");
                    btn.setOnAction(e -> checkAnswer(answer));
                    reponseBox.getChildren().add(btn);
                }
            });
        }
    }

    private void checkAnswer(String chosen) {
        reponseBox.getChildren().forEach(node -> node.setDisable(true));

        if (chosen.equals(correctReponse)) {
            score++;
            scoreLabel.setText(score + "/5");
            vraiFauxLabel.setText("Correct !");
            vraiFauxLabel.setStyle("-fx-text-fill: green;");
        } else {
            vraiFauxLabel.setText("Mauvaise réponse ! C'était : " + correctReponse);
            vraiFauxLabel.setStyle("-fx-text-fill: red;");
        }

        suivantButton.setVisible(true);
    }

    @FXML
    private void onNext() {
        if (score >= 5) {
            passerAuDialogueIntermediaire();
        } else {
            chargerNouvelleQuestion();
        }
    }

    private void passerAuDialogueIntermediaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogue-intermediaire.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) scoreLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du dialogue intermédiaire : " + e.getMessage());
        }
    }
}
