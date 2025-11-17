package io.github.HenriqueLopes_dev.controller;

import io.github.HenriqueLopes_dev.model.Userr;
import io.github.HenriqueLopes_dev.security.AuthUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    default Userr getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        return (Userr) authentication.getPrincipal();
    }
    default boolean isAuthenticated() {
        return AuthUtils.isAuthenticated();
    }
}
