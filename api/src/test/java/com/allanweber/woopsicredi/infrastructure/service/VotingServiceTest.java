package com.allanweber.woopsicredi.infrastructure.service;

import com.allanweber.woopsicredi.config.ModelMapperConfig;
import com.allanweber.woopsicredi.domain.dto.voting.VotingResponseDto;
import com.allanweber.woopsicredi.domain.dto.voting.VotingResquestDto;
import com.allanweber.woopsicredi.domain.dto.voting.VotingResultResponseDto;
import com.allanweber.woopsicredi.domain.entity.Answer;
import com.allanweber.woopsicredi.domain.entity.Ruling;
import com.allanweber.woopsicredi.domain.entity.Vote;
import com.allanweber.woopsicredi.domain.entity.Voting;
import com.allanweber.woopsicredi.domain.exception.ApiException;
import com.allanweber.woopsicredi.domain.exception.DataNotFoundedException;
import com.allanweber.woopsicredi.domain.exception.UserAreadyVotedException;
import com.allanweber.woopsicredi.infrastructure.repository.RulingRepository;
import com.allanweber.woopsicredi.infrastructure.repository.VotingRepository;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class VotingServiceTest {

    @Mock
    private VotingRepository repository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private RulingRepository rulingRepository;

    @InjectMocks
    private VotingService service;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(service, "mapper", new ModelMapperConfig().modelMapper());
    }

    @Test
    public void shouldThrowExceptionWhenVotingRepositoryIsNull() {
        expectedException.expectMessage("VotingRepository is required.");
        expectedException.expect(NullPointerException.class);
        new VotingService(null, null, null);
    }

    @Test
    public void shouldThrowExceptionWhenRulingRepositoryIsNull() {
        expectedException.expectMessage("RulingRepository is required.");
        expectedException.expect(NullPointerException.class);
        new VotingService(repository, null, null);
    }

    @Test
    public void shouldThrowExceptionWhenModelMapperIsNull() {
        expectedException.expectMessage("ModelMapper is required.");
        expectedException.expect(NullPointerException.class);
        new VotingService(repository, rulingRepository, null);
    }

    @Test
    public void shouldGetListOfVotings(){
        Ruling ruling = new Ruling( "name");
        Voting voting1 = new Voting(ruling, 1);
        Voting voting2 = new Voting(ruling, 1);
        List<Voting> votings = Arrays.asList(voting1, voting2);

        Mockito.when(repository.findAll()).thenReturn(votings);

        List<VotingResponseDto> response = service.getVotings();
        assertEquals(2, response.size());
    }

    @Test
    public void shouldThrowExceptionWhenVotingNotFounded(){
        expectedException.expectMessage("Voting id not founded");
        expectedException.expect(DataNotFoundedException.class);
        ObjectId objectId = new ObjectId();
        Mockito.when(repository.findById(objectId)).thenThrow(new DataNotFoundedException("Voting id not founded"));
        service.getVoting(objectId.toHexString());
    }

    @Test
    public void shouldReturnVoting(){
        ObjectId objectId = new ObjectId();
        Voting voting = new Voting(new Ruling("name"), 1);
        ReflectionTestUtils.setField(voting, "id", objectId);

        Mockito.when(repository.findById(objectId)).thenReturn(Optional.of(voting));

        VotingResponseDto response = service.getVoting(objectId.toHexString());
        assertEquals(response.getId(), voting.getId().toHexString());
    }

    @Test
    public void shouldThrowExceptionWhenCreatingVotingWithInvalidRuling(){
        expectedException.expectMessage("Ruling id not founded");
        expectedException.expect(DataNotFoundedException.class);
        ObjectId objectId = new ObjectId();

        Mockito.when(rulingRepository.findById(objectId)).thenThrow(new DataNotFoundedException("Ruling id not founded"));

        VotingResquestDto dto = new VotingResquestDto();
        dto.setRulingId(objectId.toHexString());

        service.createVoting(dto);
    }

    @Test
    public void shouldCreateNewVoting(){

        ObjectId objectId = new ObjectId();
        Ruling ruling = new Ruling("name");
        ReflectionTestUtils.setField(ruling, "id", objectId);

        Voting voting = new Voting(ruling, 1);

        VotingResquestDto request = new VotingResquestDto();
        request.setRulingId(objectId.toHexString());
        request.setExpiration(1);

        Mockito.when(rulingRepository.findById(objectId)).thenReturn(Optional.of(ruling));

        Mockito.when(repository.insert(voting)).thenReturn(voting);

        VotingResponseDto response = service.createVoting(request);

        assertEquals(response.getRuling().getId(), voting.getRuling().getId().toHexString());
    }

    @Test
    public void shouldAddVote(){
        ObjectId objectId = new ObjectId();
        Ruling ruling = new Ruling("name");
        ReflectionTestUtils.setField(ruling, "id", objectId);
        Vote vote = new Vote("mail@mail.com", Answer.SIM);
        Voting voting = new Voting(ruling, 1);

        Mockito.when(repository.findById(objectId)).thenReturn(Optional.of(voting));

        Voting votingWithVote = new Voting(ruling,  1);
        votingWithVote.addVote(vote);
        Mockito.when(repository.save(voting)).thenReturn(votingWithVote);

        service.addVote(objectId.toHexString(), vote);
    }

    @Test
    public void whenAddVoteWithInvalidVotingIdShouldReturnException(){
        expectedException.expectMessage("Voting id not founded");
        expectedException.expect(DataNotFoundedException.class);

        ObjectId objectId = new ObjectId();

        Mockito.when(repository.findById(objectId)).thenThrow(new DataNotFoundedException("Voting id not founded"));

        Vote vote = new Vote("mail@mail.com", Answer.SIM);
        service.addVote(objectId.toHexString(), vote);
    }

    @Test
    public void whenAddEqualVoteShouldReturnException(){
        expectedException.expectMessage("The user id 'mail@mail.com' have already voted for this voting 'name'");
        expectedException.expect(UserAreadyVotedException.class);

        ObjectId objectId = new ObjectId();
        Ruling ruling = new Ruling("name");
        ReflectionTestUtils.setField(ruling, "id", objectId);
        Vote vote = new Vote("mail@mail.com", Answer.SIM);
        Voting voting = new Voting(ruling, 1);
        voting.addVote(vote);

        Mockito.when(repository.findById(objectId)).thenReturn(Optional.of(voting));

        service.addVote(objectId.toHexString(), new Vote("mail@mail.com", Answer.NAO));
    }

    @Test
    public void shouldCountVotes(){
        ObjectId objectId = new ObjectId();
        Ruling ruling = new Ruling("name");
        ReflectionTestUtils.setField(ruling, "id", objectId);
        Voting voting = new Voting(ruling, 1);

        for (int i = 0; i < 100; i++) {
            voting.addVote(
                    new Vote(String.format("meuemail%s@mail.com", i),
                            i % 2 == 0? Answer.SIM : Answer.NAO)
            );
        }

        Mockito.when(repository.findById(objectId)).thenReturn(Optional.of(voting));

        VotingResultResponseDto result = service.getResult(objectId.toHexString());

        assertEquals(50, result.getYes().intValue());
        assertEquals(50, result.getNo().intValue());
    }

    @Test
    public void shouldCountVotesZero(){
        ObjectId objectId = new ObjectId();
        Ruling ruling = new Ruling("name");
        ReflectionTestUtils.setField(ruling, "id", objectId);
        Voting voting = new Voting(ruling, 1);

        Mockito.when(repository.findById(objectId)).thenReturn(Optional.of(voting));

        VotingResultResponseDto result = service.getResult(objectId.toHexString());

        assertEquals(0, result.getYes().intValue());
        assertEquals(0, result.getNo().intValue());
    }

    @Test
    public void addVotingExpiredShouldReturnException(){
        expectedException.expectMessage("This voting has already expired.");
        expectedException.expect(ApiException.class);
        ObjectId objectId = new ObjectId();
        Ruling ruling = new Ruling("name");
        ReflectionTestUtils.setField(ruling, "id", objectId);
        Voting voting = new Voting(ruling, 1);
        ReflectionTestUtils.setField(voting, "expirationDate", DateUtils.addMinutes(new Date(), -2));

        Mockito.when(repository.findById(objectId)).thenReturn(Optional.of(voting));
        Mockito.when(repository.save(voting)).thenReturn(voting);

        service.addVote(objectId.toHexString(), new Vote("mail@mail.com", Answer.NAO));
    }
}