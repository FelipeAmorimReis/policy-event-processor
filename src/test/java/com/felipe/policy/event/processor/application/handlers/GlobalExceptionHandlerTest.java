package com.felipe.policy.event.processor.application.handlers;

import static org.junit.jupiter.api.Assertions.*;

import com.felipe.policy.event.processor.application.dto.response.ApiError;
import com.felipe.policy.event.processor.application.exceptions.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Test
    void businessExceptionIsHandledCorrectly() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/test");

        BusinessException exception = new BusinessException("Business error occurred");
        ResponseEntity<ApiError> response = handler.handleBusinessException(exception, request);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Business error occurred", response.getBody().getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        Assertions.assertEquals("Bad Request", response.getBody().getError());
        Assertions.assertEquals("/test", response.getBody().getPath());
    }

    @Test
    void entityNotFoundExceptionIsHandledCorrectly() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/test");

        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        ResponseEntity<ApiError> response = handler.handleEntityNotFound(exception, request);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Entity not found", response.getBody().getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        Assertions.assertEquals("Not Found", response.getBody().getError());
        Assertions.assertEquals("/test", response.getBody().getPath());
    }

    @Test
    void unexpectedExceptionIsHandledCorrectly() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/test");

        Exception exception = new Exception("Unexpected error occurred");
        ResponseEntity<ApiError> response = handler.handleUnexpected(exception, request);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Unexpected error occurred", response.getBody().getMessage());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        Assertions.assertEquals("Internal Server Error", response.getBody().getError());
        Assertions.assertEquals("/test", response.getBody().getPath());
    }
}