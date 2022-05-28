package com.example.laboratuvar_raporlama.security;

import org.springframework.security.core.GrantedAuthority;

public class SimpleGrantedAuthority implements GrantedAuthority {

    private final String role;

    public SimpleGrantedAuthority(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
