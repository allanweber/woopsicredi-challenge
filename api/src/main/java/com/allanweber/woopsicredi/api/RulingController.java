package com.allanweber.woopsicredi.api;

import com.allanweber.woopsicredi.domain.dto.ruling.RulingRequestDto;
import com.allanweber.woopsicredi.domain.dto.ruling.RulingResponseDto;
import com.allanweber.woopsicredi.domain.exception.ExceptionResponse;
import com.allanweber.woopsicredi.domain.service.IRulingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "ruling", produces = "application/json")
@Api(value="ruling", description="Operations to manage ruling")
@ApiResponses(value = {
        @ApiResponse(code = 400, message = "An error occurred", response = ExceptionResponse.class)
})
@Validated
public class RulingController {

    private IRulingService service;

    @Autowired
    public RulingController(IRulingService service) {
        this.service = Objects.requireNonNull(service, "RulingService is required.");
    }

    @ApiOperation(value = "List all rulings", response = RulingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get rulings")
    })
    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(service.getRulings());
    }

    @ApiOperation(value = "Get one ruling", response = RulingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get ruling")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@NotNull @PathVariable("id") String id){
        return ResponseEntity.ok(service.getRuling(id));
    }

    @ApiOperation(value = "Create a ruling", response = RulingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully create a ruling")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RulingRequestDto ruling) throws URISyntaxException {
        RulingResponseDto created = service.createRuling(ruling);
        return ResponseEntity.created(new URI(created.getId())).body(created);
    }
}
