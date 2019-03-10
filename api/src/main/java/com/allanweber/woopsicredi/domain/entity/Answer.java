package com.allanweber.woopsicredi.domain.entity;


import javax.validation.constraints.NotNull;

public enum Answer {

    SIM("SIM"),
    NAO("NAO");

    private String vote;

    Answer(@NotNull String vote) {
        this.vote = vote;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}
