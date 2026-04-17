package com.example.javaproject;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuizController {
    @FXML private Label labelQuestion, labelScore, labelFeedback;
    @FXML private VBox containerReponses;

    private int score = 0;
    private String reponseCorrecte;
    private Gson gson = new Gson();

    @FXML
    public void initialize() {
        chargerNouvelleQuestion();
    }

    private void chargerNouvelleQuestion(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://opentdb.com/api_config.php?amount=1&type=multiple"))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::traiterReponseAPI)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }
}