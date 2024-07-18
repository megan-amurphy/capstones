package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.security.RegisterUserDto;
import com.techelevator.tenmo.model.security.User;

import java.util.List;

public interface UserDao {

    List<User> getUsers();

    User getUserById(int id);

    User getUserByUsername(String username);

    User createUser(RegisterUserDto user);
}
