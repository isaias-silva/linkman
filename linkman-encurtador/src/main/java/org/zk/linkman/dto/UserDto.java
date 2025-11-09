package org.zk.linkman.dto;

import java.util.List;

public record UserDto(
        Long id,
        String name,
        String mail,
        List<LinkDto> links
) {
}
