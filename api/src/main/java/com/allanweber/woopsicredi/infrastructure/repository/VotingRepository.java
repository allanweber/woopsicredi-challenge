package com.allanweber.woopsicredi.infrastructure.repository;

import com.allanweber.woopsicredi.domain.entity.Voting;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VotingRepository extends MongoRepository<Voting, ObjectId> {
}
