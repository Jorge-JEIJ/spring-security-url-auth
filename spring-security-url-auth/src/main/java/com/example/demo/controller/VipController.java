package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/vip")
@PreAuthorize("hasRole('VIP')")
public class VipController {

    @GetMapping("/")
    public String vipLounge() {
        return "VIP lounge room";
    }

    @GetMapping("/welcome")
    public String vipWelcome() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
        return "Welcome, " + username + ", you deserve to be here because you are " + roles.toArray()[0];
    }
}
