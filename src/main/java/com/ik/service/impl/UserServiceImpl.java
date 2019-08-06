package com.ik.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ik.dao.RoleDao;
import com.ik.dao.UserDao;
import com.ik.model.Role;
import com.ik.model.User;
import com.ik.service.UserService;


@Service(value = "userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired 
	private RoleDao roleDao;
	
	Logger logger = Logger.getLogger(UserServiceImpl.class);

	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		logger.info("Enter into loadUserByUsername()");
		User user = userDao.findByUsername(userId);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
	}

	private List<SimpleGrantedAuthority> getAuthority(User user) {
		//return Arrays.asList(new SimpleGrantedAuthority(user.getRoles().get(0).getName()));
		return Arrays.asList(new SimpleGrantedAuthority(user.getRoles().get(0).getName()));
	}

	public List<User> findAll() {
		List<User> list = new ArrayList<>();
		userDao.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public void delete(long id) {
		userDao.delete(id);
	}

	@Override
    public User save(User user) {
        return userDao.save(user);
    }

	@Override
	public Role findOne(Integer id) {
		return roleDao.findOne(id);
	}

	@Override
	public User getUser(String username) {
		logger.info("Enter into getUser() method");
		User user = userDao.findByUsername(username);
		return user;
	}
}
