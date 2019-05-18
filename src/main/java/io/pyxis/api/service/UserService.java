package io.pyxis.api.service;

import io.pyxis.api.domain.model.ApplicationUser;

import java.util.List;

public interface UserService {

    ApplicationUser signUp(ApplicationUser user);

    List<ApplicationUser> findAll();

    ApplicationUser findByUserName(String username);

    ApplicationUser findById(Long id);

    void deleteById(Long id);


}
