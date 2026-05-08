package stock_dashboard.stock_dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stock_dashboard.stock_dashboard.model.Recommendation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final StockService stockService;

    public Recommendation getRecommendation(String symbol){
        List<Double> prices = stockService.getHistoricalPrices(symbol, 30);

        double shortMA = movingAverage(prices, 5);
        double longMA = movingAverage(prices, 20);
        double rsi = calculateRSI(prices, 14);

        String signal;
        String reason;

        if(rsi < 30){
            signal = "BUY";
            reason = String.format("RSI is %.1f - stock is oversold", rsi);
        }else if(rsi > 70){
            signal = "SELL";
            reason = String.format("RSI is %.1f - stock is overbought", rsi);

        }else if(shortMA > longMA){
            signal = "BUY";
            reason = String.format("5-day MA (%.2f) crossed above 20-day MA (%.2f)", shortMA, longMA);
        }else{
            signal = "HOLD";
            reason = String.format("No strong signal. RSI: %.1f, MA spread: %.2f", rsi, shortMA - longMA);
        }

        return Recommendation.builder()
                .symbol(symbol)
                .signal(signal)
                .reason(reason)
                .rsi(rsi)
                .shortMA(shortMA)
                .longMA(longMA)
                .build();
    }

    // Simple Moving Average
    private double movingAverage(List<Double> prices, int period){
        return prices.stream()
                .limit(period)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    // RSI = 100 - (100 / (1 + RS)) where RS = avg gain / avg loss
    private double calculateRSI(List<Double> prices, int period){
        double gains = 0, losses = 0;

        for (int i = 1; i <= period && i < prices.size(); i++){
            double diff = prices.get(i - 1) - prices.get(i);
            if(diff > 0) gains += diff;
            else         losses -= diff;
        }

        double avgGain = gains / period;
        double avgLoss = losses / period;

        if (avgLoss == 0) return 100;
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }
}
