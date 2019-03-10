package com.allanweber.woopsicredi.domain.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class Vote {

    @NotNull
    @Pattern(regexp = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$")
    private String userId;

    @NotNull
    private Answer answer;

    public Vote() {
    }

    public Vote(@NotNull String userId, @NotNull Answer answer) {
        this.userId = userId;
        this.answer = answer;
    }

    public String getUserId() {
        return userId;
    }

    public Answer getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(userId, vote.userId) &&
                answer == vote.answer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, answer);
    }
}
