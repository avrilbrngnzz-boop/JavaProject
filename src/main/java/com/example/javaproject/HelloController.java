package com.example.javaproject;

import com.example.javaproject.model.DialogueLine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Label personnageLabel;
    @FXML
    private Label textLabel;

    private ArrayList<DialogueLine> dialogues = new ArrayList<>();
    private int currentIndex = 0;
    private int charIndex = 0;
    private Timeline animation;
    private boolean isAnimating = false;

    @FXML
    public void initialize() {
        dialogues.add(new DialogueLine("Chef", "Écoute-moi bien. Une bombe a été placée quelque part en ville, et tout repose sur toi. \n" + "Nous n'avons pas de temps à perdre. Chaque seconde compte. "));
        dialogues.add(new DialogueLine("Chef", "Voici la situation : tu vas devoir résoudre une série de questions. Chacune te donnera des " + "indices pour localiser la bombe. Le temps presse, mais nous avons encore une chance" + "si tu agis rapidement et avec précision."));
        dialogues.add(new DialogueLine("Chef", "Je sais que ce n'est pas facile, mais je crois en toi. Nous avons les outils nécessaires, et \n" + "tu as l'intelligence pour déchiffrer ces énigmes. Chaque réponse correcte nous \n" + "rapproche de la solution. "));
        dialogues.add(new DialogueLine("Chef", "Ne laisse pas la pression te faire trébucher. Résous les énigmes, trouve l’emplacement \n" + "de la bombe, et nous pourrons la désamorcer avant qu'il ne soit trop tard. On compte \n" + "sur toi. La ville compte sur toi."));
        textLabel.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.SPACE) {
                        handleSpace();
                    }
                });
            }
        });
        showDialogue(0);
    }

    private void showDialogue(int index) {
        DialogueLine line = dialogues.get(index);
        personnageLabel.setText(line.getPersonnage() + " :");
        textLabel.setText("");
        charIndex = 0;
        isAnimating = true;
        if (animation != null) {
            animation.stop();
        }
        String fullText = line.getText();

        animation = new Timeline(
                new KeyFrame(Duration.millis(35), e -> {
                    if (charIndex < fullText.length()) {
                        textLabel.setText(fullText.substring(0, charIndex + 1));
                        charIndex++;
                    } else {
                        animation.stop();
                        isAnimating = false;
                    }
                })
        );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void handleSpace() {
        if (isAnimating) {
            animation.stop();
            textLabel.setText(dialogues.get(currentIndex).getText());
            isAnimating = false;
        } else {
            currentIndex++;
            if (currentIndex < dialogues.size()) {
                showDialogue(currentIndex);
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz-view.fxml"));
                    Stage stage = (Stage) textLabel.getScene().getWindow();
                    stage.setScene(new Scene(loader.load(), 800, 500));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}