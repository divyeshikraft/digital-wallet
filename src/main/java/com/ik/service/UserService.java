package com.ik.service;

import com.ik.model.Role;
import com.ik.model.User;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(User user);
    List<User> findAll();
    void delete(long id);
    User getUser(String username);
    
    Role findOne(Integer integer);
    
}
