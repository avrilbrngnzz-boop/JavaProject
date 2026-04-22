package com.example.javaproject;
import javax.xml.transform.Result;
import java.util.List

public class QuizQuestion {
    public List<Result> results;
    public static class Result {
        public String question;
        public String correct_answer;
        public List<String> incorrect_answers;
    }
}
