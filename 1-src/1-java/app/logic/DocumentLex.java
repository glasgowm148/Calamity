/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import Utils.Sentence;
import Utils.SentenceToken;
import Utils.StanfordAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Punkid PC
 */
public class DocumentLex {
    private final HashMap<String, Integer> vector;

    public DocumentLex() throws IOException {
        System.out.println("test");
        BufferedReader bufReader = new BufferedReader(new FileReader("/../../../src/conf/lex.txt"));

        ArrayList<String> lex = new ArrayList<>();
        String line = bufReader.readLine();
        while (line != null) {
            lex.add(line);
            line = bufReader.readLine();
        }
        bufReader.close();

        vector = new HashMap<>();
        for(String l: lex){
            vector.put(l, 0);
        }
    }




    public HashMap<String, Integer> makeDocumentLex(String note){
        try{
            StanfordAnalysis stanford = new StanfordAnalysis(note);
            for(Sentence s: stanford.getLemmatizedPOS(note)){
                for(SentenceToken token: s.getTokens()){
                    if(vector.containsKey(token.getToken().toLowerCase()))
                        vector.replace(token.getToken().toLowerCase(), vector.get(token.getToken().toLowerCase())+1);
                }
            }
        }
        catch(IllegalStateException ignored){

        }

        return vector;

    }




}