package com.syndica.backend.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.syndica.backend.validation.ValidCpf;
import lombok.Builder;

@Builder
public record UserForCreateDTO (
    @NotBlank(message = "Full name is required")
    String fullname,
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    String email,
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must contain between 8 and 255 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
        message = "Password must contain a lowercase letter, an uppercase letter, a number, and a special character"
    )
    String password,
    @NotBlank(message = "CPF is required")
    @ValidCpf
    String cpf,
    @NotBlank(message = "Theme is required")
    @Pattern(
        regexp = "SYSTEM|LIGHT|DARK",
        message = "Theme must be SYSTEM, LIGHT, or DARK"
    )
    String theme
) {
    public UserForCreateDTO {
        if (theme == null) {
            theme = "SYSTEM";
        }
    }
}
