package com.fitfit.research.dto;

public record AuthResponse(
    String token,
    String username,
    String fullName,
    boolean admin
) {}
