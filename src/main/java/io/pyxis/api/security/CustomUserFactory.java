package io.pyxis.api.security;

import io.pyxis.api.domain.model.ApplicationUser;
import io.pyxis.api.domain.model.Role;
import io.pyxis.api.domain.model.Status;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class CustomUserFactory {

    public static CustomUser create(ApplicationUser user) {
        return CustomUser.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .enabled(user.getStatus().equals(Status.ACTIVE))
                        .lastModifiedPassword(user.getUpdated())
                        .authorities(mapToGrandedAuthority(user.getRoles()))
                        .build();
    }

    private static Collection<GrantedAuthority> mapToGrandedAuthority(List<Role> roles){
        return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
    }

}
