package com.example.javaproject;

import java.util.List;

public class QuizFR {

    public List<Quiz> quizzes;

    public static class Quiz {
        public String question;
        public String answer;        // <-- correspond à "answer"
        public List<String> badAnswers; // <-- correspond à "badAnswers"
    }
}
