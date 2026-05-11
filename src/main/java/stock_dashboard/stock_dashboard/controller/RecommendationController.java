package stock_dashboard.stock_dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock_dashboard.stock_dashboard.model.Recommendation;
import stock_dashboard.stock_dashboard.service.RecommendationService;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor

public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{symbol}")
    public ResponseEntity<Recommendation> getRecommendation(@PathVariable String symbol){
        return ResponseEntity.ok(recommendationService.getRecommendation(symbol.toUpperCase()));
    }
}
