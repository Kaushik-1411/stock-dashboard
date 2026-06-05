package stock_dashboard.stock_dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stock_dashboard.stock_dashboard.Util.JwtUtil;
import stock_dashboard.stock_dashboard.model.User;
import stock_dashboard.stock_dashboard.repository.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(String username, String password, String email){

        System.out.println("username = " + username);
        System.out.println("password = " + password);
        System.out.println("email = " + email);

        if (userRepository.existsByUsername(username)){
            throw new RuntimeException("Username already taken: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        System.out.println("Saving user...");

        userRepository.save(user);

//        System.out.println("Generating token...");
//
//        return jwtUtil.generateToken(username);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(username);
    }
}
