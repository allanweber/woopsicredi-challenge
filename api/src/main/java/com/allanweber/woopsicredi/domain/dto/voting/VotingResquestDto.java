package com.allanweber.woopsicredi.domain.dto.voting;

import javax.validation.constraints.NotNull;

public class VotingResquestDto {

    public VotingResquestDto() {
        expiration = 1;
    }

    @NotNull
    private String rulingId;

    private Integer expiration;

    public String getRulingId() {
        return rulingId;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setRulingId(String rulingId) {
        this.rulingId = rulingId;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }
}
