package com.example.demo.controller;

import com.example.demo.model.Admin;
import com.example.demo.service.AdminService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

	@Autowired
	private AdminService adminService;

	// 註冊管理員
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Admin admin) {
	    // 校验账号和密码是否为空
	    if (admin.getAccount() == null || admin.getAccount().trim().isEmpty()) {
	        return ResponseEntity.badRequest().body("帳號不可為空");
	    }
	    if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
	        return ResponseEntity.badRequest().body("密碼不可為空");
	    }

	    boolean isRegistered = adminService.register(admin);
	    if (isRegistered) {
	        return ResponseEntity.ok("管理員註冊成功");
	    } else {
	        return ResponseEntity.badRequest().body("帳號已存在");
	    }
	}


	// 登入管理員
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Admin admin) {
		boolean isAuthenticated = adminService.login(admin);
		if (isAuthenticated) {
//			String token = jsonWebTokenUtility.createToken(admin.toString());
			return ResponseEntity.ok("登入成功");
		} else {
			return ResponseEntity.badRequest().body("帳號或密碼錯誤");
		}
	}

	// 編輯管理員資訊
	@PutMapping("/edit/{id}")
	public ResponseEntity<String> edit(@PathVariable Integer id, @RequestBody Admin admin) {
		boolean isUpdated = adminService.updateAdmin(id, admin);
		if (isUpdated) {
			return ResponseEntity.ok("管理員資訊更新成功");
		} else {
			return ResponseEntity.badRequest().body("管理員不存在");
		}
	}

	// 刪除管理員
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		boolean isDeleted = adminService.deleteAdmin(id);
		if (isDeleted) {
			return ResponseEntity.ok("管理員刪除成功");
		} else {
			return ResponseEntity.badRequest().body("管理員不存在");
		}
	}

	// 根據 id 查詢管理員
	@GetMapping("/{id}")
	public ResponseEntity<Admin> getAdminById(@PathVariable Integer id) {
		Admin admin = adminService.getAdminById(id);
		if (admin != null) {
			return ResponseEntity.ok(admin);
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	// 列出所有管理員
	@GetMapping("/all")
	public ResponseEntity<List<Admin>> getAllAdmins() {
		List<Admin> admins = adminService.getAllAdmins();
		if (!admins.isEmpty()) {
			return ResponseEntity.ok(admins);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
}
