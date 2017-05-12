package com.m.chittorgarh.Model;

import java.util.ArrayList;

/**
 * Created by Admins on 5/11/2017.
 */

public class NavData {
    String id,title;
    ArrayList<NevSubData> subTitile = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<NevSubData> getSubTitile() {
        return subTitile;
    }

    public void setSubTitile(ArrayList<NevSubData> subTitile) {
        this.subTitile = subTitile;
    }
}
