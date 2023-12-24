package com.eva.demo.service;

import com.eva.demo.domain.Users;

public interface UsersService {
    Users findByID(Long userID);

    void save(Users user);
}
