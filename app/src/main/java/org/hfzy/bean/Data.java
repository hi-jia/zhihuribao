package org.hfzy.bean;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/7/15 0015.
 */

public class Data {

    private String date;
    private String background;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    private ArrayList<ListItem> stories;
    private ArrayList<TopList> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ListItem> getStories() {
        return stories;
    }

    public void setStories(ArrayList<ListItem> stories) {
        this.stories = stories;
    }

    public ArrayList<TopList> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(ArrayList<TopList> top_stories) {
        this.top_stories = top_stories;
    }
}
