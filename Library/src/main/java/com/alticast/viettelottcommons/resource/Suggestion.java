package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class Suggestion {
    private ArrayList<String> related_terms = null;
    private String corrected_term= null;

    public ArrayList<String> getRelated_terms() {
        return related_terms;
    }

    public String getCorrected_term() {
        return corrected_term;
    }
}
