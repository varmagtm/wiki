package com.moodys.ma.wiki.handler;

import com.moodys.ma.wiki.api.model.ErrorResponseDTO;
import com.moodys.ma.wiki.exception.DataAccessException;
import com.moodys.ma.wiki.exception.NoDataFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class DocumentExceptionHandler {

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoDataFoundException(
            NoDataFoundException ex, HttpServletRequest request) {
        log.error("DocumentExceptionHandler received NoDataFoundException");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponseDTO(LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                ex.getMessage(), request.getRequestURI())
                );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataAccessException(
            DataAccessException ex, HttpServletRequest request) {
        log.error("DocumentExceptionHandler received event for DataAccessException");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponseDTO(LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                ex.getMessage(), request.getRequestURI())
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(
            Exception ex, HttpServletRequest request) {
        log.error("DocumentExceptionHandler received event for Exception");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponseDTO(LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                ex.getMessage(), request.getRequestURI())
                );
    }
}
