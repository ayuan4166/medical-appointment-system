package com.yuan.medical.controller;

import com.yuan.medical.entity.User;
import com.yuan.medical.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User created = userService.registerPatient(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            if ("REGISTER_FORBIDDEN".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("权限不足");
            }
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("用户名或密码不能为空");
        }

        username = username.trim();
        password = password.trim();

        System.out.println("=== 登录尝试 ===");
        System.out.println("输入的用户名：" + username);
        System.out.println("输入的密码：" + password);

        List<User> users = userService.findAll();
        System.out.println("数据库中的用户数：" + users.size());

        for (User user : users) {
            System.out.println("检查用户：" + user.getUsername() + " | 密码：" + user.getPassword());
            if (user.getUsername().equals(username)) {
                System.out.println("用户名匹配！");
                if (user.getPassword().equals(password)) {
                    System.out.println("密码匹配！登录成功");
                    return ResponseEntity.ok(user);
                } else {
                    System.out.println("密码不匹配！");
                    System.out.println("  数据库密码：" + user.getPassword());
                    System.out.println("  输入密码：" + password);
                }
            }
        }

        System.out.println("登录失败");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("用户名或密码错误");
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
