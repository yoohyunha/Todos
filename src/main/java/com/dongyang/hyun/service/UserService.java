package com.dongyang.hyun.service;

import com.dongyang.hyun.dto.UserDto;
import com.dongyang.hyun.entity.User;
import com.dongyang.hyun.entity.UserRole;
import com.dongyang.hyun.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signup(UserDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return null;
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setRole(UserRole.USER);
        return userRepository.save(user);
    }

    // 추가된 login 메서드
    public User login(UserDto dto) {
        return userRepository.findByUsername(dto.getUsername())
                .filter(u -> passwordEncoder.matches(dto.getPassword(), u.getPassword()))
                .orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }public User updateProfile(Long userId, UserDto dto) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        return userRepository.save(user);
    }
    public List<User> searchByUsername(String keyword, Long excludeId) {
        return userRepository.findByUsernameContainingAndIdNot(keyword, excludeId);
    }


}
