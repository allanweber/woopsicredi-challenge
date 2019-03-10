package com.allanweber.woopsicredi.domain.service;

import com.allanweber.woopsicredi.domain.dto.ruling.RulingRequestDto;
import com.allanweber.woopsicredi.domain.dto.ruling.RulingResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface IRulingService {

    List<RulingResponseDto> getRulings();

    RulingResponseDto getRuling(@NotNull String id);

    RulingResponseDto createRuling(@Valid RulingRequestDto dto);
}
