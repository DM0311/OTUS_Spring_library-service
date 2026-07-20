package com.otus.library.library_service.dto.request;

import com.otus.library.library_service.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UserReqDto {

    @NotBlank(message = "e-mail обязателен")
    private String email;

    @NotBlank(message = "имя пользователя обязателен")
    private String userName;

    @NotBlank(message = "ФИО пользователя обязателен")
    private String fullName;

    private String password;

    private Set<Role> roles;
}
