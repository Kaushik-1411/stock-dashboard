package stock_dashboard.stock_dashboard.controller;

import lombok.RequiredArgsConstructor;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import stock_dashboard.stock_dashboard.model.Portfolio;
import stock_dashboard.stock_dashboard.service.PortfolioService;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor

public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<List<Portfolio>> getPortfolio(Authentication auth){
        return ResponseEntity.ok(portfolioService.getPortfolio(auth.getName()));
    }

    @GetMapping("/summary")
    public ResponseEntity<PortfolioService.PortfolioSummary> getSummary(Authentication auth){
        return ResponseEntity.ok(portfolioService.getSummary(auth.getName()));
    }

    @PostMapping("/add")
    public ResponseEntity<Portfolio> addStock(@RequestBody Portfolio portfolio, Authentication auth){
        portfolio.setUserId(auth.getName());
        return ResponseEntity.ok(portfolioService.addStock(portfolio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStock(
            @PathVariable Long id,
            Authentication auth) {
        portfolioService.removeStock(id);
        return ResponseEntity.noContent().build();
    }
}
