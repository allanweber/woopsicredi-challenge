package com.allanweber.woopsicredi.domain.service;

import com.allanweber.woopsicredi.domain.dto.voting.VotingResponseDto;
import com.allanweber.woopsicredi.domain.dto.voting.VotingResquestDto;
import com.allanweber.woopsicredi.domain.dto.voting.VotingResultResponseDto;
import com.allanweber.woopsicredi.domain.entity.Vote;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface IVotingService {

    List<VotingResponseDto> getVotings();

    VotingResponseDto getVoting(@NotNull String id);

    VotingResponseDto createVoting(@Valid VotingResquestDto dto);

    void addVote(@NotNull String id, @Valid Vote vote);

    VotingResultResponseDto getResult(@NotNull String id);
}
