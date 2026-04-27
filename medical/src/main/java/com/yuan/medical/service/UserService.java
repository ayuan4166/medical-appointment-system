package com.yuan.medical.service;

import com.yuan.medical.entity.User;
import com.yuan.medical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在: id=" + id));
    }

    @Transactional
    public User create(User user) {
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            user.setPassword("123456");
        }
        return userRepository.save(user);
    }

    /**
     * 公开注册：仅允许患者（USER）。若请求中指定为医生或管理员则拒绝。
     */
    @Transactional
    public User registerPatient(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        String role = user.getRole();
        if (role != null && !role.trim().isEmpty() && !"USER".equalsIgnoreCase(role.trim())) {
            throw new IllegalStateException("REGISTER_FORBIDDEN"); // 由控制器映射为 403
        }

        String username = user.getUsername().trim();
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已被占用");
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(user.getPassword().trim());
        u.setName(user.getName().trim());
        u.setPhone(user.getPhone() != null && !user.getPhone().trim().isEmpty()
                ? user.getPhone().trim() : null);
        u.setEmail(user.getEmail() != null && !user.getEmail().trim().isEmpty()
                ? user.getEmail().trim() : null);
        u.setRole("USER");
        return userRepository.save(u);
    }

    @Transactional
    public User update(Long id, User userDetails) {
        User user = findById(id);
        if (userDetails.getUsername() != null) {
            String username = userDetails.getUsername().trim();
            if (!username.isEmpty() && !username.equals(user.getUsername())) {
                if (userRepository.existsByUsername(username)) {
                    throw new IllegalArgumentException("用户名已被占用");
                }
                user.setUsername(username);
            }
        }
        if (userDetails.getName() != null) user.setName(userDetails.getName().trim());
        if (userDetails.getPhone() != null) user.setPhone(userDetails.getPhone().trim());
        if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail().trim());
        if (userDetails.getRole() != null) user.setRole(userDetails.getRole().trim());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("用户不存在: id=" + id);
        }
        userRepository.deleteById(id);
    }
}
