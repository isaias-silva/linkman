package org.zk.linkman.encurtador.dto;

import java.util.List;

public record UserDto(
        Long id,
        String name,
        String mail
) {
}
