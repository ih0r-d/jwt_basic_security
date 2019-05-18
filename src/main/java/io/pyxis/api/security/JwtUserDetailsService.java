package io.pyxis.api.security;

import io.pyxis.api.domain.model.ApplicationUser;
import io.pyxis.api.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException(name));

        log.info("loadUserByUsername --> User by username: {} successfully loaded", name);

        return CustomUserFactory.create(user);
    }
}
