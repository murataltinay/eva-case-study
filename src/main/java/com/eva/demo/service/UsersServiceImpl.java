package com.eva.demo.service;

import com.eva.demo.domain.Users;
import com.eva.demo.exceptions.BusinessException;
import com.eva.demo.exceptions.ReasonType;
import com.eva.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public Users findByID(Long userID) {
        Optional<Users> usersOptional = usersRepository.findById(userID);
        if(usersOptional.isEmpty()) {
            throw new BusinessException(ReasonType.USER_NOT_FOUND);
        }
        return usersOptional.get();
    }

    @Override
    public void save(Users user) {
        usersRepository.save(user);
    }
}
