package org.zk.linkman.dto;

import io.smallrye.common.constraint.NotNull;

public record LoginDto(@NotNull String mail, @NotNull String password) {
}
