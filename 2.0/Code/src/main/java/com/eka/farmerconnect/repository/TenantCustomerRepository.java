package com.eka.farmerconnect.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eka.farmerconnect.entity.TenantCustomer;

@Repository
public interface TenantCustomerRepository extends JpaRepository<TenantCustomer, Integer>{
	
	@Query("select tc from tenant_customer tc where tc.username in (?1) and tc.clientId = ?2 and tc.isDelete=0")
	List<TenantCustomer> findActiveCustomersByUsername(Set<String> usernames, Integer clientId);

}
