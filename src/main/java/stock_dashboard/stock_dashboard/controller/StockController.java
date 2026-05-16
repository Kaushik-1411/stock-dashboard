package stock_dashboard.stock_dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stock_dashboard.stock_dashboard.model.Stock;
import stock_dashboard.stock_dashboard.model.StockHistory;
import stock_dashboard.stock_dashboard.model.StockSearchResult;
import stock_dashboard.stock_dashboard.service.StockService;

import java.util.List;

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

}
