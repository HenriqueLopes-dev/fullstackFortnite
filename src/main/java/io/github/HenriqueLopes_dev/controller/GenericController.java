package io.github.HenriqueLopes_dev.controller;

import io.github.HenriqueLopes_dev.security.AuthUtils;
import jakarta.servlet.Servlet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

public interface GenericController {

    default URI generateHeaderLocation(UUID id){
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

    default boolean isOwner(UUID targetId) {
        return AuthUtils.isOwner(targetId);
    }

    default boolean isAuthenticated() {
        return AuthUtils.isAuthenticated();
    }
}
