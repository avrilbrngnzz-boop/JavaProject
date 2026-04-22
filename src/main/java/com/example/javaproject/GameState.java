package com.example.javaproject;

public class GameState {
    private int currentStep;

    public GameState(int currentStep) {
        this.currentStep = currentStep;
    }

    // Getters et Setters (utile pour Gson)
    public int getCurrentStep() { return currentStep; }
    public void setCurrentStep(int currentStep) { this.currentStep = currentStep; }
}

