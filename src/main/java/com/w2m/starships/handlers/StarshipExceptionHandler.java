package com.w2m.starships.handlers;

import com.w2m.starships.models.exceptions.StarshipNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StarshipExceptionHandler {

    @ExceptionHandler
    public ErrorResponseException handleStarshipNotFound(StarshipNotFoundException ex) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ErrorResponseException(HttpStatus.NOT_FOUND, problemDetail, ex);
    }

    @ExceptionHandler
    public ErrorResponseException handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        final String errorMessage = String.join(", ", ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
        return new ErrorResponseException(HttpStatus.BAD_REQUEST, problemDetail, ex);
    }


    


}
