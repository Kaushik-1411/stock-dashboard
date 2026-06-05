package stock_dashboard.stock_dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stock_dashboard.stock_dashboard.model.Stock;
import stock_dashboard.stock_dashboard.model.StockHistory;
import stock_dashboard.stock_dashboard.model.StockMover;
import stock_dashboard.stock_dashboard.model.StockSearchResult;
import stock_dashboard.stock_dashboard.service.StockService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/stock")
@RequiredArgsConstructor

public class StockController {

    private final StockService stockService;

    @GetMapping("/{symbol}")
    public ResponseEntity<Stock> getStock(@PathVariable String symbol){
        System.out.println("Symbol : " + symbol);
        return ResponseEntity.ok(stockService.getStockQuote(symbol.toUpperCase()));
    }

    @GetMapping("/{symbol}/history")
    public ResponseEntity<List<StockHistory>> getHistory(
        @PathVariable String symbol,
        @RequestParam(defaultValue = "30") int days){
        return ResponseEntity.ok(stockService.getHistory(symbol.toUpperCase(), days));
    }

    @GetMapping("/search")
    public ResponseEntity<List<StockSearchResult>> search(@RequestParam String q) {
        return ResponseEntity.ok(stockService.searchStocks(q));
    }



    @GetMapping("/gainers")
    public ResponseEntity<List<StockMover>> getTopGainers() {
        return ResponseEntity.ok(stockService.getTopGainers());
    }

    @GetMapping("/losers")
    public ResponseEntity<List<StockMover>> getTopLosers() {
        return ResponseEntity.ok(stockService.getTopLosers());
    }

    @GetMapping("/active")
    public ResponseEntity<List<StockMover>> getMostActive() {
        return ResponseEntity.ok(stockService.getMostActive());
    }

    // Single endpoint that returns all three — useful for dashboard
    @GetMapping("/overview")
    public ResponseEntity<Map<String, List<StockMover>>> getMarketOverview() {
        return ResponseEntity.ok(Map.of(
                "gainers",  stockService.getTopGainers(),
                "losers",   stockService.getTopLosers(),
                "active",   stockService.getMostActive()
        ));
    }

}
