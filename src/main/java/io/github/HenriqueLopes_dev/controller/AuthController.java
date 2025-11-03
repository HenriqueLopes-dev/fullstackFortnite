package io.github.HenriqueLopes_dev.controller;

import io.github.HenriqueLopes_dev.dto.auth.AuthRequestDTO;
import io.github.HenriqueLopes_dev.dto.user.UserDTO;
import io.github.HenriqueLopes_dev.model.Userr;
import io.github.HenriqueLopes_dev.security.JwtService;
import io.github.HenriqueLopes_dev.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request, HttpServletResponse response) {

        Userr user = userService.authenticate(request.getEmail(), request.getPassword());
        String token = jwtService.generateToken(user);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 24h
        response.addCookie(cookie);

        return ResponseEntity.ok("Login realizado");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logout realizado");
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me() {
        // Pega o usuário autenticado do SecurityContext
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Userr user) {
            // Retorna os dados que você quer no DTO
            return ResponseEntity.ok(new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getBalance()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
