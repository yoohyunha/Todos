package com.dongyang.hyun.controller;

import com.dongyang.hyun.dto.LoginDto;
import com.dongyang.hyun.dto.TokenDto;
import com.dongyang.hyun.dto.UserDto;
import com.dongyang.hyun.entity.User;
import com.dongyang.hyun.security.TokenProvider;
import com.dongyang.hyun.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
//JWT 기반 인증 API 컨트롤러
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UserDto userDto) {
        User user = userService.signup(userDto);
        return (user != null) ?
                ResponseEntity.ok("회원가입 성공") :
                ResponseEntity.badRequest().body("회원가입 실패");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String jwt = tokenProvider.createToken(authentication);

        return ResponseEntity.ok(new TokenDto(jwt));
    }
}
