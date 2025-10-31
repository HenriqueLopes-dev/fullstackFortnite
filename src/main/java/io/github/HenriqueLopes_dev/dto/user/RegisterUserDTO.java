package io.github.HenriqueLopes_dev.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(
        @NotBlank(message = "O campo nome é obrigatório!")
        @Size(min = 2, max = 50, message = "O nome deve ter de 2 a 50 caracteres!")
        String name,

        @NotBlank(message = "O campo email é obrigatório!")
        @Email
        @Size(min = 5, max = 254, message = "O email deve ter de 5 a 254 caracteres!")
        String email,

        @NotBlank
        @Size(min = 8, message = "A senha deve ter no minimo 8 caracteres!")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\d\\s]).+$",
                message = "A senha deve conter pelo menos uma letra minúscula, uma maiúscula, um número e um caractere especial!"
        )
        String password
        ){
}
