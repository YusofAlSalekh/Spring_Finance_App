package com.yusof.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@RequiredArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    private final static String PREFIX = "ROLE_";
    private final ClientRole clientRole;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CustomGrantedAuthority that = (CustomGrantedAuthority) o;
        return clientRole == that.clientRole;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clientRole);
    }

    @Override
    public String getAuthority() {
        return PREFIX + clientRole.name();
    }
}
