package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class SearchItem {
    String click_token;
    String id;
    int purchases;
    Program program;
    //Schedule
    public String start_time;
    public String end_time;
    public MultiLingualText[] title;
    public MultiLingualText[] synopsis;
    public MultiLingualText[] directors_text;
    public MultiLingualText[] actors_text;
    public CustomValue[] custom;

    //Program

    public int number;
    public String service_id;
    public String service_type;
    public Product[] product;
    public Channel channel;

    public String numberStr;


    //Menu
    String pid;
    String[] path;
    MultiLingualText[] name;
    CustomValue[] config;
    String type;
    String updated_time;
}
