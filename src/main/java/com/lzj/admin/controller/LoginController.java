package com.lzj.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lzj.admin.pojo.User;
import com.lzj.admin.service.UserService;

@Controller
public class LoginController {
	
	@Resource
    private UserService userService;

    /**
     * 登录接口 - 验证数据库
     */
    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 1. 查询用户
        User user = userService.findForName(username);

        // 2. 验证用户是否存在
        if (user == null) {
            result.put("code", 500);
            result.put("message", "用户名不存在");
            return result;
        }

        // 3. 验证密码（数据库中的密码是明文还是加密？）
        // 如果数据库存的是明文密码：
        if (!password.equals(user.getPassword())) {
            result.put("code", 500);
            result.put("message", "密码错误");
            return result;
        }

        // 4. 检查用户是否被禁用
        if (user.getIsDel() != null && user.getIsDel() == 1) {
            result.put("code", 500);
            result.put("message", "用户已被禁用");
            return result;
        }

        // 5. 登录成功
        session.setAttribute("loginUser", user);
        session.setAttribute("username", username);

        result.put("code", 0);
        result.put("message", "登录成功");
        result.put("obj", user);

        return result;
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @ResponseBody
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "退出成功");
        return result;
    }
}