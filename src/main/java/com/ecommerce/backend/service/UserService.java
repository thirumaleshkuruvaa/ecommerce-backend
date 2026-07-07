package com.ecommerce.backend.service;

import com.ecommerce.backend.model.User;

public interface UserService {

    User findUserByJwtToken(String jwt);

    User findUserByEmail(String emial);
}
