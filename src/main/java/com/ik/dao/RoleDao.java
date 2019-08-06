package com.ik.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ik.model.Role;

@Repository(value="roleDao")
public interface RoleDao extends CrudRepository<Role, Integer> {
    
	Role findOne(Integer id);
    
}
