package com.alvesdev.medsched_api.dto.response.user;

import java.util.UUID;

public record UserDetailResDto(
    UUID id,
    String username,
    String emailString,

    ProfileResDto profile
) { 
}
