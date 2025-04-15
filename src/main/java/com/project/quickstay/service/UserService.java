package com.project.quickstay.service;

import com.project.quickstay.domain.user.dto.SelectUserType;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User inputUserType(User user, SelectUserType selectUserType) {
        User getUser = getUserById(user.getId());
        getUser.changeUserType(selectUserType.getUserType());
        return getUser;
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ServiceException("유저가 없습니다"));
    }
}
