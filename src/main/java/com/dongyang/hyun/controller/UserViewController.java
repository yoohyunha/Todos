package com.dongyang.hyun.controller;

import com.dongyang.hyun.dto.UserDto;
import com.dongyang.hyun.entity.User;
import com.dongyang.hyun.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserViewController {
    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() { return "user/login"; }

    @PostMapping("/login")
    public String login(UserDto dto, HttpSession session, Model model) {
        User user = userService.login(dto);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/todos";
        }
        model.addAttribute("loginError", true);
        return "user/login";
    }

    @GetMapping("/signup")
    public String signupPage() { return "user/signup"; }

    @PostMapping("/signup")
    public String signup(UserDto dto, Model model) {
        User user = userService.signup(dto);
        if (user != null) {
            return "redirect:/login";
        }
        model.addAttribute("signupError", true);
        return "user/signup";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "user/mypage";
    }

    @PostMapping("/mypage/update")
    public String updateProfile(UserDto dto, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";
        User updated = userService.updateProfile(sessionUser.getId(), dto);
        session.setAttribute("user", updated);
        model.addAttribute("user", updated);
        model.addAttribute("updateSuccess", true);
        return "user/mypage";
    }
}
