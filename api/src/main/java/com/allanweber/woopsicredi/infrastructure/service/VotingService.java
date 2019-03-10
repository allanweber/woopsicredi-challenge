package com.allanweber.woopsicredi.infrastructure.service;

import com.allanweber.woopsicredi.domain.dto.voting.VotingResponseDto;
import com.allanweber.woopsicredi.domain.dto.voting.VotingResquestDto;
import com.allanweber.woopsicredi.domain.dto.voting.VotingResultResponseDto;
import com.allanweber.woopsicredi.domain.entity.Answer;
import com.allanweber.woopsicredi.domain.entity.Ruling;
import com.allanweber.woopsicredi.domain.entity.Vote;
import com.allanweber.woopsicredi.domain.entity.Voting;
import com.allanweber.woopsicredi.domain.exception.ApiException;
import com.allanweber.woopsicredi.domain.exception.DataNotFoundedException;
import com.allanweber.woopsicredi.domain.service.IVotingService;
import com.allanweber.woopsicredi.infrastructure.repository.RulingRepository;
import com.allanweber.woopsicredi.infrastructure.repository.VotingRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Validated
public class VotingService implements IVotingService {

    private VotingRepository repository;
    private RulingRepository rulingRepository;
    private ModelMapper mapper;

    @Autowired
    public VotingService(VotingRepository repository, RulingRepository rulingRepository, ModelMapper mapper) {
        this.repository = Objects.requireNonNull(repository, "VotingRepository is required.");
        this.rulingRepository = Objects.requireNonNull(rulingRepository, "RulingRepository is required.");
        this.mapper = Objects.requireNonNull(mapper, "ModelMapper is required.");
    }

    @Override
    public List<VotingResponseDto> getVotings() {
        List<Voting> votings = repository.findAll();
        return votings.stream().map(voting -> mapper
                .map(voting, VotingResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public VotingResponseDto getVoting(@NotNull String id) {
        Voting voting = getOne(id);
        return mapper.map(voting, VotingResponseDto.class);
    }

    @Override
    public VotingResponseDto createVoting(@Valid VotingResquestDto dto) {

        Ruling ruling = rulingRepository.findById(new ObjectId(dto.getRulingId()))
                .orElseThrow(() -> new DataNotFoundedException("Ruling id not founded"));

        Voting voting = new Voting(ruling, dto.getExpiration());
        voting = repository.insert(voting);
        return mapper.map(voting, VotingResponseDto.class);
    }

    @Override
    public void addVote(@NotNull String id, @Valid Vote vote) {
        Voting voting = getOne(id);

        if(voting.isExpired()){
            repository.save(voting);
            throw new ApiException("This voting has already expired.");
        }

        voting.addVote(vote);

        repository.save(voting);
    }

    @Override
    public VotingResultResponseDto getResult(@NotNull String id) {
        Voting voting = getOne(id);

        Long yes = voting.getVotes().stream().filter(vote -> vote.getAnswer().equals(Answer.SIM)).count();
        Long no = voting.getVotes().stream().filter(vote -> vote.getAnswer().equals(Answer.NAO)).count();

        return new VotingResultResponseDto(yes, no);
    }

    private Voting getOne(@NotNull String id){
        return repository.findById(new ObjectId(id))
                .orElseThrow(() -> new DataNotFoundedException("Voting id not founded"));
    }
}
