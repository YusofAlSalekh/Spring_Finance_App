package com.yusof.web.security;

import com.yusof.web.entity.ClientModel;
import com.yusof.web.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ClientModel clientModel = clientRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User " + email + " not found."));
        return new CustomUserDetails(
                clientModel.getId(),
                clientModel.getEmail(),
                clientModel.getPassword(),
                clientModel.getRoles()
                        .stream()
                        .map(CustomGrantedAuthority::new)
                        .collect(Collectors.toList()));
    }
}
