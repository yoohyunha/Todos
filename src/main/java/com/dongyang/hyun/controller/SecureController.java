package com.dongyang.hyun.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SecureController {

    @GetMapping("/secure-list")
    @PreAuthorize("hasRole('USER')")
    public List<String> getSecureList() {
        return List.of(
                "인증된 사용자만 볼 수 있는 데이터 1",
                "인증된 사용자만 볼 수 있는 데이터 2",
                "인증된 사용자만 볼 수 있는 데이터 3"
        );
    }

    @GetMapping("/admin-list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<String> getAdminList() {
        return List.of(
                "관리자만 볼 수 있는 데이터 1",
                "관리자만 볼 수 있는 데이터 2",
                "시스템 관리 정보"
        );
    }
}
