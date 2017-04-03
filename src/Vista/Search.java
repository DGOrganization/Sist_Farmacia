/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import java.text.Normalizer;

/**
 *
 * @author Gerard
 */
public class Search { 
  public static String highlight(String text, String search)
  {
    int i = normalize(text).indexOf(normalize(search));
    if (i != -1) {
      return      
        text.substring(0, i).concat("<b color='blue'>").concat(text.substring(i, i + search.length())).concat("</b>").concat(text.substring(i + search.length()));
    }
    return text;
  }  
  public static String normalize(String text)
  {
    return 
      Normalizer.normalize(text.toUpperCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
  }
}
