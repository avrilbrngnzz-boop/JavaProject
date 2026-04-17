package com.example.javaproject.model;

public class DialogueLine {
    private String personnage;
    private String text;

    public DialogueLine(String personnage, String text){
        this.personnage = personnage;
        this.text = text;
    }

    public String getPersonnage(){
        return personnage;
    }

    public String getText(){
        return text;
    }
}
