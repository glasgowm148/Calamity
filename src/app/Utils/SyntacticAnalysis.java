package Utils;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Punkid PC
 */
public abstract class SyntacticAnalysis {

    public String text;

    public SyntacticAnalysis(String t){
        text = t;
    }

    public abstract List<String> getSentences(String text);
    public abstract ArrayList<Sentence> getAnnotatedPOS(String text);
    public abstract ArrayList<Sentence> getLemmatizedPOS(String text);

}