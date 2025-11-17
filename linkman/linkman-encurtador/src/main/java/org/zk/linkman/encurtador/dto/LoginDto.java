package org.zk.linkman.encurtador.dto;

import io.smallrye.common.constraint.NotNull;

public record LoginDto(@NotNull String mail, @NotNull String password) {
}
