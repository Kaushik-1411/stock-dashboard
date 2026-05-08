package stock_dashboard.stock_dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock_dashboard.stock_dashboard.model.Stock;
import stock_dashboard.stock_dashboard.service.StockService;

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
}
