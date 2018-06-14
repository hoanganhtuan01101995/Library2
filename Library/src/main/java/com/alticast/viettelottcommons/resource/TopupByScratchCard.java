package com.alticast.viettelottcommons.resource;


public class TopupByScratchCard {
    private String card_serial = null;
    private String card_pin = null;
    private String promotion_id = null;

    public TopupByScratchCard(String card_serial, String card_pin, String promotion_id) {
        this.card_serial = card_serial;
        this.card_pin = card_pin;
        this.promotion_id = promotion_id;
    }

    public String getCard_serial() {
        return card_serial;
    }

    public void setCard_serial(String card_serial) {
        this.card_serial = card_serial;
    }

    public String getCard_pin() {
        return card_pin;
    }

    public void setCard_pin(String card_pin) {
        this.card_pin = card_pin;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    @Override
    public String toString() {
        return "TopupByScratchCard{" +
                "card_serial='" + card_serial + '\'' +
                ", card_pin='" + card_pin + '\'' +
                ", promotion_id='" + promotion_id + '\'' +
                '}';
    }
}
