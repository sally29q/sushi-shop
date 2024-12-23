package com.example.demo.repository;

import com.example.demo.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	// 根據帳號查詢管理員
	Admin findByAccount(String account);

	// 檢查帳號是否存在
	boolean existsByAccount(String account);

	// 根據 id 查詢管理員
	@Query(value = "SELECT * FROM admin WHERE admin_id = :adminId", nativeQuery = true)
    Admin findAdminById(Integer adminId);
	
	// 根據 id 刪除管理員
	@Query(value = "DELETE FROM admin WHERE admin_id = :adminId", nativeQuery = true)
	void deleteByAdminId(Integer adminId);
	



}
