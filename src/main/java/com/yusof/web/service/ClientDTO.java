package com.yusof.web.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class ClientDTO {
    private int id;
    private String email;

    @Override
    public String toString() {
        return "ClientDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientDTO clientDTO = (ClientDTO) o;
        return id == clientDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}