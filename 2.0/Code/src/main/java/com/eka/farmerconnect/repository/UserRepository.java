package com.eka.farmerconnect.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eka.farmerconnect.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query("select u from User u where u.username in (?1) and u.clientId = ?2 and u.isActive=1 and u.isDelete=0")
	List<User> findActiveUsersByUsername(Set<String> usernames, Integer clientId);
	
	@Query("select u.clientId from User u where u.id =?1 and u.isActive=1 and u.isDelete=0")
	Integer  findTenantIdByUserId(Integer userId);
	
	@Query(nativeQuery = true, value="select u.ID, u.FIRST_NAME, u.LAST_NAME, u.CLIENT_ID, u.USERNAME, "
			+ "group_concat(urm.ROLE_ID), group_concat(r.NAME), r.IS_SYSTEM_CREATED "
			+ "from users u "
			+ "join user_role_mappings urm on u.ID=urm.USER_ID "
			+ "join roles r on urm.ROLE_ID=r.ID "
			+ "where u.IS_ACTIVE = 1 "
			+ "and u.IS_DELETE = 0 "
			+ "and u.CLIENT_ID = ?2 "
			+ "and urm.CLIENT_ID = ?2 "
			+ "and r.CLIENT_ID = ?2 "
			+ "and u.USERNAME in (?1) "
			+ "group by u.ID, u.FIRST_NAME, u.LAST_NAME, u.CLIENT_ID, u.USERNAME, r.IS_SYSTEM_CREATED ")
	List<Object[]> findUserAndRoleInfo(Set<String> usernames, Integer clientId);

}
