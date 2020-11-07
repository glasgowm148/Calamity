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

    public DocumentBOW(String docPath, ArrayList<String> lex, String sPath){
        path = docPath;
        vector = new HashMap<>();
        sWordPath = sPath;
        for(String l: lex){
            vector.put(l, 0);
        }
    }

    void makeDocumentBOW(){
        /*
        MedicalNotePreprocessing preProcessing = new MedicalNotePreprocessing(path, sWordPath);
        preProcessing.segmentFile();
        preProcessing.eliminateNoise(".*\\d+|_+|:|;|\\*+|,|-|\\[|\\]|#|\\(|\\).*", "");
        preProcessing.eliminateStopWords();
        */
        String[] testString = { "Athens residents flee as strong earthquake shakes Greek capital. A strong earthquake has struck near Athens, causing residents of the Greek capital to run into the streets.",
            "Athens residents flee as strong earthquake shakes Greek capital. A strong earthquake has struck near Athens,"};
        for(String note : testString){
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
        }

    }




}