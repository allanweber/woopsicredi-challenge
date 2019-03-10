package com.allanweber.woopsicredi.domain.exception;

import com.allanweber.woopsicredi.domain.entity.Ruling;

public class UserAreadyVotedException extends RuntimeException {
    public UserAreadyVotedException(String userId, Ruling ruling) {
        super(String.format("The user id '%s' have already voted for this voting '%s'", userId, ruling.getName()));
    }
}
