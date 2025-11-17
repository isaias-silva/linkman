package org.zk.linkman.encurtador.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


public record UpdateUserDto(
        @Email String mail,
        @Size(max = 20, min = 3) String name,
        @Size(max = 12, min = 5) String password) {
}
