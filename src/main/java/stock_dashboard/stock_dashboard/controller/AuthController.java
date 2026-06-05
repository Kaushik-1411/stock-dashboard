package stock_dashboard.stock_dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stock_dashboard.stock_dashboard.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> body) {

        authService.register(
                body.get("username"),
                body.get("password"),
                body.get("email")
        );

        return ResponseEntity.ok(Map.of("message", "User registered successfully. You can login now."));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String , String>> login(@RequestBody Map<String, String> body) {
        String token = authService.login(
                body.get("username"),
                body.get("password")
        );
        return ResponseEntity.ok(Map.of("token", token));
    }

}
