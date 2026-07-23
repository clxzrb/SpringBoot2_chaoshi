package com.lzj.admin.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SecurityControllerAdvice {
    
    @ModelAttribute("SpringSecurity")
    public Authentication getSpringSecurity() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}