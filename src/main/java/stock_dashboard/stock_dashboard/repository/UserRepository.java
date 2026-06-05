package stock_dashboard.stock_dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stock_dashboard.stock_dashboard.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
