package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.ads.Placement;
import com.alticast.viettelottcommons.resource.ads.PlacementDecision;

import java.util.ArrayList;

public class AdDecisionRes {
    String messageIdRef;//: "f8098332-57fb-402d-9ce6-cf62b266334c",
    ArrayList<displayAd> displayAdDecision;

    public String getMessageIdRef() {
        return messageIdRef;
    }

    public void setMessageIdRef(String messageIdRef) {
        this.messageIdRef = messageIdRef;
    }

    public ArrayList<displayAd> getDisplayAdDecision() {
        return displayAdDecision;
    }

    public void setDisplayAdDecision(ArrayList<displayAd> displayAdDecision) {
        this.displayAdDecision = displayAdDecision;
    }

    public class displayAd {
        public ArrayList<Placement> getDisplayAd() {
            return displayAd;
        }

        public void setDisplayAd(ArrayList<Placement> displayAd) {
            this.displayAd = displayAd;
        }

        ArrayList<Placement> displayAd;
    }
}
