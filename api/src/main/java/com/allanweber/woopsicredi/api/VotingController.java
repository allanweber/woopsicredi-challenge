package com.allanweber.woopsicredi.api;

import com.allanweber.woopsicredi.domain.dto.voting.VotingResponseDto;
import com.allanweber.woopsicredi.domain.dto.voting.VotingResquestDto;
import com.allanweber.woopsicredi.domain.dto.voting.VotingResultResponseDto;
import com.allanweber.woopsicredi.domain.entity.Answer;
import com.allanweber.woopsicredi.domain.entity.Vote;
import com.allanweber.woopsicredi.domain.exception.ExceptionResponse;
import com.allanweber.woopsicredi.domain.service.IVotingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@RestController
@RequestMapping(value = "voting", produces = "application/json")
@Api(value = "voting", description = "Operations to manage voting")
@ApiResponses(value = {
        @ApiResponse(code = 400, message = "An error occurred", response = ExceptionResponse.class)
})
@Validated
public class VotingController {

    private IVotingService service;

    public VotingController(IVotingService service) {
        this.service = Objects.requireNonNull(service, "IVotingService is required.");
    }

    @ApiOperation(value = "List all votings", response = VotingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get votings")
    })
    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(service.getVotings());
    }

    @ApiOperation(value = "Get one voting", response = VotingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get voting")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@NotNull @PathVariable("id") String id){
        return ResponseEntity.ok(service.getVoting(id));
    }

    @ApiOperation(value = "Get one voting result", response = VotingResultResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get voting result")
    })
    @GetMapping("/result/{id}")
    public ResponseEntity<?> getResult(@NotNull @PathVariable("id") String id){
        return ResponseEntity.ok(service.getResult(id));
    }

    @ApiOperation(value = "Create a voting", response = VotingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully create a voting")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody VotingResquestDto voting) throws URISyntaxException {
        VotingResponseDto created = service.createVoting(voting);
        return ResponseEntity.created(new URI(created.getId())).body(created);
    }

    @ApiOperation(value = "Add vote to a voting")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully Add vote to a voting")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/vote/{id}/{answer}")
    public ResponseEntity<?> addVote(@NotNull @RequestHeader("user-id") String userId,
                                      @NotNull @PathVariable("id") String id,
                                      @Valid @PathVariable("answer") Answer answer) {

        Vote vote = new Vote(userId, answer);
        service.addVote(id, vote);
        return ResponseEntity.ok().build();
    }
}
