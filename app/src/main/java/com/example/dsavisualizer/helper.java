package com.example.dsavisualizer;

public class helper {
    private String title;
    private String worstCom;
    private String bestCom;
    private String aveCom;

    private String description;

    public helper(String title, String worstCom, String bestCom, String aveCom, String description) {
        this.title = title;
        this.worstCom = worstCom;
        this.bestCom = bestCom;
        this.aveCom = aveCom;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getWorstCom() {
        return worstCom;
    }

    public String getBestCom() {
        return bestCom;
    }

    public String getAveCom() {
        return aveCom;
    }

}
