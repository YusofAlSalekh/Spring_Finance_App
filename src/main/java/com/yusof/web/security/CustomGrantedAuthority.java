package com.yusof.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    private final static String PREFIX = "ROLE_";
    private final ClientRole clientRole;

    @Override
    public String getAuthority() {
        return PREFIX + clientRole.name();
    }
}
