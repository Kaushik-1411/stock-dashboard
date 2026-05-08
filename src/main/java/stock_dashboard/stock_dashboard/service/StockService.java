package stock_dashboard.stock_dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import stock_dashboard.stock_dashboard.model.Stock;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Value("${alphavantage.base.url}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;

    public Stock getStockQuote(String symbol) {
        WebClient client = webClientBuilder.baseUrl(baseUrl).build();

        Map response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function", "GLOBAL_QUOTE")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey",apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, String> quote = (Map<String, String>) response.get("Global Quote");

        return Stock.builder()
                .symbol(quote.get("01. symbol"))
                .price(Double.parseDouble(quote.get("05. price")))
                .change(Double.parseDouble(quote.get("09. change")))
                .changePercent(parsePercent(quote.get("10. change percent")))
                .volume(Long.parseLong(quote.get("06. volume")))
                .build();
    }

    public List<Double> getHistoricalPrices(String symbol, int days){
        WebClient client = webClientBuilder.baseUrl(baseUrl).build();

        Map response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function", "TIME_SERIES_DAILY")
                        .queryParam("symbol", symbol)
                        .queryParam("outputsize", "compact")
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Map<String, String>> timeSeries =
                (Map<String, Map<String, String>>) response.get("Time Series (Daily)");

        return timeSeries.values().stream()
                .limit(days)
                .map(day -> Double.parseDouble(day.get("4. close")))
                .toList();
    }

    private double parsePercent(String s){
        return Double.parseDouble(s.replace("%", "").trim());
    }
}
