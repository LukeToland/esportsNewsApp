package com.project.opticproject;

/**
 * Created by Luke on 05/09/2016.
 */
public class TableData {

    String rank;
    String team;
    String wins;
    String losses;
    String winPerc;

    public TableData(String rank, String team, String wins, String losses, String winPerc) {
        this.rank = rank;
        this.winPerc = winPerc;
        this.losses = losses;
        this.wins = wins;
        this.team = team;
    }

    public String getRank() {
        return rank;
    }

    public String getWinPerc() {
        return winPerc;
    }

    public String getLosses() {
        return losses;
    }

    public String getWins() {
        return wins;
    }

    public String getTeam() {
        return team;
    }
}
