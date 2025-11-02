package org.zk.linkman.dto;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
                            @NotNull @Email String mail,
                            @NotNull @Size(max = 20, min = 3) String name,
                            @NotNull @Size(max = 12, min = 5) String password) {
}
