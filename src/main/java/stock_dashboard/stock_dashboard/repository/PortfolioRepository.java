package stock_dashboard.stock_dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stock_dashboard.stock_dashboard.model.Portfolio;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByUserId(String userId);
    void deleteByIdAndUserId(Long id, String userId);
}
