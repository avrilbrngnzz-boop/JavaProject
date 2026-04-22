package com.example.javaproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DialogueIntermediaireController {

    @FXML private StackPane rootPane;
    @FXML private Label labelNom;
    @FXML private Label labelTexte;

    private List<String> phrases = new ArrayList<>();
    private int indexPhraseActuelle = 0;
    private String phraseComplete = "";
    private Timeline timeline;

    @FXML
    public void initialize() {
        labelNom.setText("Chef");

        // Ajout des répliques obligatoires du sujet
        phrases.add("Bien joué. Tu as bien avancé jusqu'ici. Tu as résolu toutes les énigmes...");
        phrases.add("Maintenant, il te faut localiser l'emplacement exact. Pour cela, tu vas interroger des suspects.");
        phrases.add("Le temps presse. Chaque erreur pourrait nous coûter cher, alors fais attention.");
        phrases.add("Tu es notre seul espoir, et je sais que tu peux le faire. Trouve la bombe...");
        phrases.add("Allez, il ne reste plus beaucoup de temps. Trouve cette bombe, et sauve tout le monde.");

        // On lance la première phrase
        afficherProchainePhrase();

        // Donne le focus au rootPane pour capter la touche Espace immédiatement
        Platform.runLater(() -> rootPane.requestFocus());
    }

    private void afficherProchainePhrase() {
        if (indexPhraseActuelle < phrases.size()) {
            phraseComplete = phrases.get(indexPhraseActuelle);
            lancerAnimationTexte(phraseComplete);
            indexPhraseActuelle++;
        } else {
            terminerDialogue();
        }
    }

    private void lancerAnimationTexte(String texte) {
        labelTexte.setText("");
        if (timeline != null) timeline.stop();

        timeline = new Timeline();
        for (int i = 0; i < texte.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(30 * i), // 30ms entre chaque lettre (vitesse machine à écrire)
                    e -> labelTexte.setText(labelTexte.getText() + texte.charAt(index))
            );
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            // Si l'animation est en cours, on l'arrête et on affiche tout le texte
            if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
                timeline.stop();
                labelTexte.setText(phraseComplete);
            } else {
                // Sinon, on passe à la phrase suivante
                afficherProchainePhrase();
            }
        }
    }

    private void terminerDialogue() {
        // 1. SAUVEGARDE GSON (Étape 4 = Démineur)
        sauvegarderProgression(4);

        // 2. TRANSITION VERS DÉMINEUR
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("demineur.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Étape 4 : Le Démineur");
            stage.show();
        } catch (IOException e) {
            System.err.println("ERREUR : Impossible de charger demineur.fxml");
            e.printStackTrace();
        }
    }

    private void sauvegarderProgression(int etape) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        GameState etat = new GameState(etape);

        try (FileWriter writer = new FileWriter("save.json")) {
            gson.toJson(etat, writer);
            System.out.println("Progression sauvegardée dans save.json (Étape " + etape + ")");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde JSON");
            e.printStackTrace();
        }
    }
}
