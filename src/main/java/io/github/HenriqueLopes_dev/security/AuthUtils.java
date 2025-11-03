package io.github.HenriqueLopes_dev.security;

import io.github.HenriqueLopes_dev.model.Userr;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class AuthUtils {

    public static Optional<Userr> getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Userr user) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public static boolean isAuthenticated() {
        return getAuthenticatedUser().isPresent();
    }

    public static boolean isOwner(UUID targetId) {
        return getAuthenticatedUser()
                .map(user -> user.getId().equals(targetId))
                .orElse(false);
    }

}
