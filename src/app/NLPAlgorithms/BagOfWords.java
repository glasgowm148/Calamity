/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NLPAlgorithms;

import java.io.File;
import java.util.ArrayList;


/**
 *
 * @author Punkid PC
 */
public class BagOfWords {
    private ArrayList<String> directories;
    private ArrayList<String> lexicon;
    private String stopWordsPath;

    public BagOfWords(ArrayList<String> dir, ArrayList<String> lex, String sPath){
        directories = dir;
        lexicon = lex;
        stopWordsPath = sPath;
    }

    public ArrayList<DocumentBOW> calculateBagOfWords(){
        ArrayList<DocumentBOW> documentBOW = new ArrayList<>();
        for(String path: directories){
            File directory = new File(path);
            for(final File note: directory.listFiles()){
                DocumentBOW document = new DocumentBOW(note.getPath(),lexicon, stopWordsPath);
                document.makeDocumentBOW();
                documentBOW.add(document);
            }
        }
        return documentBOW;
    }

}