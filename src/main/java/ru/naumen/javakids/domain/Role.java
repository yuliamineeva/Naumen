package ru.naumen.javakids.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author avzhukov
 * @since 17.03.2022
 */
public enum Role implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
