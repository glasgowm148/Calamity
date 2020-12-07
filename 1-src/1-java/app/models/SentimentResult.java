package models;

import classifiers.SentimentClassification;

public class SentimentResult {
    double sentimentScore;
    String sentimentType;
    SentimentClassification sentimentClass;



    public double SentimentResult(String text) {
        return getSentiment();
    }

    public double getSentiment() {
        return sentimentScore;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public String getSentimentType() {
        return sentimentType;
    }

    public void setSentimentType(String sentimentType) {
        this.sentimentType = sentimentType;
    }

    public SentimentClassification getSentimentClass() {
        return sentimentClass;
    }

    public void setSentimentClass(SentimentClassification sentimentClass) {
        this.sentimentClass = sentimentClass;
    }
}
