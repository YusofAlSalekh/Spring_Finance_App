package com.yusof.web.security;

import com.yusof.web.console.ConsoleRunner;
import com.yusof.web.entity.ClientModel;
import com.yusof.web.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.Set;

import static com.yusof.web.security.ClientRole.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomUserDetailsServiceTest {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    ClientRepository clientRepository;

    @MockitoBean
    ConsoleRunner consoleRunner;

    @Test
    void loadUserByUsername_success() {
        ClientModel model = new ClientModel();
        model.setId(1);
        model.setEmail("user@gmail.com");
        model.setPassword("hash");
        model.setRoles(Set.of(USER));

        when(clientRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(model));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("user@gmail.com");

        assertNotNull(userDetails);
        assertEquals("user@gmail.com", userDetails.getUsername());
        assertEquals("hash", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals(1, ((CustomUserDetails) userDetails).getId());
        assertEquals(new CustomGrantedAuthority(USER),
                userDetails.getAuthorities().iterator().next());

        verify(clientRepository).findByEmail("user@gmail.com");
    }

    @Test
    void loadUserByUsername_notFound_throws() {
        when(clientRepository.findByEmail("absent@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("absent@mail.com")
        );

        verify(clientRepository).findByEmail("absent@mail.com");
    }
}