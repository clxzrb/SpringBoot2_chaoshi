package com.lzj.admin.controller;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
	
	@PostMapping("/login")
	@ResponseBody  // 添加这个注解，返回JSON
	public Map<String, Object> login(@RequestParam("userName") String username,
	                                  @RequestParam("password") String password,
	                                  @RequestParam(value = "captchaCode", required = false) String captchaCode,
	                                  @RequestParam(value = "rememberMe", required = false) Boolean rememberMe,
	                                  HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		
		if ("admin".equals(username) && "123456".equals(password)) {
			// 登录成功！将用户信息放入session
			session.setAttribute("loginUser", username);
			result.put("code", 200);
			result.put("message", "登录成功");
		} else {
			// 登录失败
			result.put("code", 500);
			result.put("message", "用户名密码错误");
		}
		return result;
	}
}