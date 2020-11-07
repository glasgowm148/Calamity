/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NLPAlgorithms;

import Utils.Sentence;
import Utils.SentenceToken;
import Utils.StanfordAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Punkid PC
 */
public class DocumentBOW {
    private String name;
    private String path;
    private String sWordPath;
    private HashMap<String, Integer> vector;

    public DocumentBOW(){
        ArrayList<String> lex = new ArrayList<String>();
        lex.add("hurricane");
        lex.add("earthquake");
       // path = docPath;
        vector = new HashMap<>();
        //sWordPath = sPath;
        for(String l: lex){
            vector.put(l, 0);
        }
    }


    public HashMap<String, Integer> makeDocumentBOW(String note){
        try{
            StanfordAnalysis stanford = new StanfordAnalysis(note);
            for(Sentence s: stanford.getLemmatizedPOS(note)){
                for(SentenceToken token: s.getTokens()){
                    if(vector.containsKey(token.getToken().toLowerCase()))
                        vector.replace(token.getToken().toLowerCase(), vector.get(token.getToken().toLowerCase())+1);
                }
            }
        }
        catch(IllegalStateException e){

        }

        return vector;

    }




}