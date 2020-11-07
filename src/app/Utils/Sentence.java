/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.ArrayList;

/**
 *
 * @author erick
 */
public class Sentence {

    private ArrayList<SentenceToken> tokens;

    public Sentence(){
        tokens = new ArrayList<>();
    }

    public Sentence(ArrayList<SentenceToken> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<SentenceToken> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<SentenceToken> tokens) {
        this.tokens = tokens;
    }

    private void addToken(SentenceToken token){
        tokens.add(token);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(SentenceToken t : tokens){
            sb.append(t.getToken()).append("(").append(t.getTag()).append(") ");
        }
        return sb.toString();
    }



}