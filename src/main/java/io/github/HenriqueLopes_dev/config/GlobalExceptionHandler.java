package io.github.HenriqueLopes_dev.config;



import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.HenriqueLopes_dev.dto.error.FieldErrorDTO;
import io.github.HenriqueLopes_dev.dto.error.ResponseErrorDTO;
import io.github.HenriqueLopes_dev.exception.DuplicatedRegistryException;
import io.github.HenriqueLopes_dev.exception.NotEnoughTokensException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseErrorDTO handleMethodNotValidException(MethodArgumentNotValidException e){

        List<FieldError> fieldErrors = e.getFieldErrors();
        List<FieldErrorDTO> errorsList = fieldErrors
                .stream()
                .map(fieldError ->
                        new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage())
                ).toList();
        return new ResponseErrorDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validacao",
                errorsList
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorDTO handleHttpMessageNotReadable(HttpMessageNotReadableException e){
        return new ResponseErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de formatação!",
                List.of(new FieldErrorDTO("JSON", e.getMostSpecificCause().getMessage()))
        );
    }

    @ExceptionHandler(DuplicatedRegistryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseErrorDTO handleDuplicatedRegistryException(DuplicatedRegistryException e){
        return new ResponseErrorDTO(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                List.of());
    }

    @ExceptionHandler(NotEnoughTokensException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseErrorDTO handleNotEnoughTokensException(DuplicatedRegistryException e){
        return new ResponseErrorDTO(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                List.of());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseErrorDTO handleAcessDeniedException(AccessDeniedException e){
        return new ResponseErrorDTO(
                HttpStatus.FORBIDDEN.value(),
                "Você não possui permissão para isso!",
                List.of()
        );
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorDTO handleJsonProcessingException(JsonProcessingException e){
        return new ResponseErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de processamento de JSON!",
                List.of()
        );
    }



    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseErrorDTO handleInternalServerError(RuntimeException e){
        return new ResponseErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inexperado, tente novamente mais tarde!",
                List.of()
        );
    }


}
