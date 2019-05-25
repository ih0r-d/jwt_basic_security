package io.pyxis.api.service.impl;

import io.pyxis.api.domain.model.ApplicationUser;
import io.pyxis.api.domain.model.Status;
import io.pyxis.api.domain.repository.RoleRepository;
import io.pyxis.api.domain.repository.UserRepository;
import io.pyxis.api.exception.UnauthorizedException;
import io.pyxis.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final static String USER_ROLE = "ROLE_USER";



    @Override
    public ApplicationUser signUp(ApplicationUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepository.findByName(USER_ROLE));
        user.setStatus(Status.ACTIVE);
        ApplicationUser signedUser = userRepository.save(user);
        log.info("signUp --> User: {} successfully registered", signedUser);
        return signedUser;
    }

    @Override
    public List<ApplicationUser> findAll() {
        List<ApplicationUser> result = userRepository.findAll();
        log.info("findAll --> Found {} users",result.size() );
        return result;
    }

    @Override
    public ApplicationUser findByUserName(String username) {
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
        log.info("findByUserName --> User: {} was found", user);
        return user;
    }

    @Override
    public ApplicationUser findById(Long id) {
        ApplicationUser user = userRepository.findById(id)
                                            .orElseThrow(UnauthorizedException::new);
        log.info("findById --> User: {} found by id: {}",user, id);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        ApplicationUser user = userRepository
                .findById(id)
                .orElseThrow(UnauthorizedException::new);
        userRepository.delete(user);

        log.info("deleteById --> User: with id: {} deleted successfully", id);
    }
}
