package com.example.demo.service;

import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// 註冊管理員
	public boolean register(Admin admin) {
		// 檢查帳號是否存在
		if (adminRepo.existsByAccount(admin.getAccount())) {
			return false; // 帳號已存在
		}

		// 加密密碼
		admin.setPassword(passwordEncoder.encode(admin.getPassword()));
		adminRepo.save(admin);
		return true;
	}

	// 登入管理員
	public boolean login(Admin admin) {
		// 查找管理員
		Admin existingAdmin = adminRepo.findByAccount(admin.getAccount());
		if (existingAdmin != null && passwordEncoder.matches(admin.getPassword(), existingAdmin.getPassword())) {
			return true; // 登入成功
		}
		return false; // 登入失敗
	}

	// 編輯管理員資訊
	public boolean updateAdmin(Integer id, Admin admin) {
		// 查找管理員
		Admin existingAdmin = adminRepo.findAdminById(id);
		if (existingAdmin == null) {
			return false; // 管理員不存在
		}

		// 更新管理員帳號（如果需要）
		if (admin.getAccount() != null && !admin.getAccount().isEmpty()) {
			existingAdmin.setAccount(admin.getAccount());
		}

		// 如果密碼被修改，則加密新密碼
		if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
			existingAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
		}

		// 更新其他資訊（例如姓名）
		if (admin.getName() != null && !admin.getName().isEmpty()) {
			existingAdmin.setName(admin.getName());
		}

		// 保存更新後的管理員
		adminRepo.save(existingAdmin);
		return true; // 更新成功
	}

	// 刪除管理員
	public boolean deleteAdmin(Integer adminId) {
	    Admin admin = adminRepo.findAdminById(adminId);
	    if (admin != null) {
	        adminRepo.delete(admin);
	        return true;
	    }
	    return false;
	}

	// 根據 id 查詢管理員
	public Admin getAdminById(Integer id) {
		return adminRepo.findAdminById(id);
	}

	// 列出所有管理員
	public List<Admin> getAllAdmins() {
		return adminRepo.findAll();
	}
}
