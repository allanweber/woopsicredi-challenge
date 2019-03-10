package com.allanweber.woopsicredi.config;

import com.allanweber.woopsicredi.domain.exception.ApiException;
import com.allanweber.woopsicredi.domain.exception.ExceptionResponse;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ApiExceptionHandlerTest {

    @Test
    public void shouldReturn400ForApiException() {
        ApiException ex = new ApiException("any");
        ResponseEntity response = new ApiExceptionHandler().handleApiException(ex);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("any", ((ExceptionResponse) response.getBody()).getMessage());
    }

}