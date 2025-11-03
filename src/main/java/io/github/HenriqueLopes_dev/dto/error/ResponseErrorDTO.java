package io.github.HenriqueLopes_dev.dto.error;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ResponseErrorDTO(int status, String message, List<FieldErrorDTO> errors) {

    public static ResponseErrorDTO defaultError(String message){
        return new ResponseErrorDTO(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ResponseErrorDTO conflictError(String message){
        return new ResponseErrorDTO(HttpStatus.CONFLICT.value(), message, List.of());
    }
}
