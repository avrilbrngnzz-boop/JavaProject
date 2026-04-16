package com.example.javaproject;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class DialogueIntermediaireController {

    @FXML private StackPane rootPane;
    @FXML private Label labelNom;
    @FXML private Label labelTexte;
    @FXML private ImageView chefImage;

    private Timeline timeline;
    private String currentPhrase = "";
    private final Queue<String> dialogueQueue = new LinkedList<>(Arrays.asList(
            "Bien joué. Tu as bien avancé jusqu'ici. Tu as résolu toutes les énigmes...",
            "Maintenant, il te faut localiser l'emplacement exact.",
            "Le temps presse. Chaque erreur pourrait nous coûter cher, alors fais attention !",
            "Trouve où elle se cache, et on pourra passer à l'étape suivante.",
            "Tu es notre seul espoir. Trouve cette bombe, et sauve tout le monde."
    ));

    @FXML
    public void initialize() {
        labelNom.setText("Chef");
        showNextPhrase();
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            // Si l'animation tourne, on finit la phrase immédiatement
            if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
                timeline.stop();
                labelTexte.setText(currentPhrase);
            } else {
                showNextPhrase();
            }
        }
    }

    private void showNextPhrase() {
        String phrase = dialogueQueue.poll();
        if (phrase != null) {
            currentPhrase = phrase;
            runTypewriterAnimation(phrase);
        } else {
            navigateToMinesweeper();
        }
    }

    private void runTypewriterAnimation(String message) {
        labelTexte.setText("");
        timeline = new Timeline();

        // Déclenche la vibration si le message contient le mot clé
        if (message.contains("Le temps presse")) {
            triggerShakeAnimation();
        }

        // Boucle pour l'effet machine à écrire (exigence du projet)
        for (int i = 0; i < message.length(); i++) {
            final int index = i;
            KeyFrame frame = new KeyFrame(Duration.millis(30 * i), e ->
                    labelTexte.setText(labelTexte.getText() + message.charAt(index))
            );
            timeline.getKeyFrames().add(frame);
        }
        timeline.play();
    }

    private void triggerShakeAnimation() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), chefImage);
        tt.setFromX(-10);
        tt.setToX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }

    private void navigateToMinesweeper() {
        try {
            URL resource = getClass().getResource("demineur.fxml");
            if (resource == null) {
                System.err.println("ERREUR : Le fichier demineur.fxml n'a pas été trouvé dans le dossier ressources.");
                return;
            }
            Parent root = FXMLLoader.load(resource);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("ERREUR : Impossible de charger la scène du Démineur. Vérifiez le fichier FXML.");
        }
    }
}

