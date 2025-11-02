package org.zk.linkman.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public record CreateUrlDto(@Pattern(regexp = "https?://.*\\.[a-zA-Z]+(/.*)?",
        message = "Use a valid URL (ex: https://exemplo.com)")
                           @NotNull(message = "Url required") String url,
                           @NotNull(message = "Title required") String title) {
}
