package com.dongyang.hyun.repository;

import com.dongyang.hyun.entity.FriendRequest;
import com.dongyang.hyun.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByToUserAndStatus(User toUser, FriendRequest.Status status);
    List<FriendRequest> findByFromUserAndStatus(User fromUser, FriendRequest.Status status);
    List<FriendRequest> findByFromUserOrToUserAndStatus(User user1, User user2, FriendRequest.Status status);
}
