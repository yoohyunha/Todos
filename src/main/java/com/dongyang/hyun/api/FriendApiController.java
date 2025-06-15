package com.dongyang.hyun.api;

import com.dongyang.hyun.entity.FriendRequest;
import com.dongyang.hyun.entity.User;
import com.dongyang.hyun.service.FriendService;
import com.dongyang.hyun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendApiController {

    private final FriendService friendService;
    private final UserService userService;

    @PostMapping("/request")
    public ResponseEntity<?> sendRequest(@RequestParam Long toUserId, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        boolean ok = friendService.sendRequest(user.getId(), toUserId);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<?> accept(@RequestParam Long requestId, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        boolean ok = friendService.acceptRequest(requestId, user.getId());
        return ok ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<?> reject(@RequestParam Long requestId, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        boolean ok = friendService.rejectRequest(requestId, user.getId());
        return ok ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping
    public List<User> friends(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        if (user == null) return List.of();
        return friendService.getFriends(user.getId());
    }

    @GetMapping("/requests")
    public List<FriendRequest> pendingRequests(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        if (user == null) return List.of();
        return friendService.getPendingRequests(user.getId());
    }
}
