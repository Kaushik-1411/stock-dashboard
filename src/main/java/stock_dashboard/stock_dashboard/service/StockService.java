package stock_dashboard.stock_dashboard.service;

//import jakarta.persistence.Cacheable;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import stock_dashboard.stock_dashboard.model.Stock;
import stock_dashboard.stock_dashboard.model.StockHistory;
import stock_dashboard.stock_dashboard.model.StockMover;
import stock_dashboard.stock_dashboard.model.StockSearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Value("${alphavantage.base.url}")
    private String baseUrl;

    @Value("${yahoo.base.url}")
    private String yahooBaseUrl;

    @Value("${app.use-mock-data:false}")   // default false — enable in properties
    private boolean useMockData;

    private final WebClient.Builder webClientBuilder;


    @Cacheable(value = "stockPrice", key = "#symbol")
    public Stock getStockQuote(String symbol) {

        if (useMockData) {
            return getMockStock(symbol);
        }

        WebClient client = webClientBuilder.baseUrl(baseUrl).build();

//        Map response = client.get()
//                .uri(uriBuilder -> uriBuilder
//                        .queryParam("function", "GLOBAL_QUOTE")
//                        .queryParam("symbol", symbol)
//                        .queryParam("apikey", apiKey)
//                        .build())
//                .retrieve()
//                .bodyToMono(Map.class)
//                .block();
//
//
//        // ← ADD THIS: log the raw response so you can see exactly what Alpha Vantage returns
//        System.out.println("Alpha Vantage raw response: " + response);
//
//        // Check for rate limit message
//        if (response.containsKey("Note")) {
//            throw new RuntimeException("Rate limited: " + response.get("Note"));
//        }
//
//        // Check for error message
//        if (response.containsKey("Information")) {
//            throw new RuntimeException("API error: " + response.get("Information"));
//        }
//
//        Map<String, String> quote = (Map<String, String>) response.get("Global Quote");
//
//        // Check for empty quote (wrong symbol)
//        if (quote == null || quote.isEmpty()) {
//            throw new RuntimeException("Symbol not found: " + symbol);
//        }
//
//        return Stock.builder()
//                .symbol(quote.get("01. symbol"))
//                .price(Double.parseDouble(quote.get("05. price")))
//                .change(Double.parseDouble(quote.get("09. change")))
//                .changePercent(parsePercent(quote.get("10. change percent")))
//                .volume(Long.parseLong(quote.get("06. volume")))
//                .build();

        try {
            Map<String, Object> response = client.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("function", "GLOBAL_QUOTE")
                            .queryParam("symbol", symbol)
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            System.out.println("Alpha Vantage raw response: " + response);

            if (response.containsKey("Note")) {
                throw new RuntimeException("Rate limited: " + response.get("Note"));
            }

            if (response.containsKey("Information")) {
                throw new RuntimeException("API error: " + response.get("Information"));
            }

            Map<String, String> quote =
                    (Map<String, String>) response.get("Global Quote");

            if (quote == null || quote.isEmpty()) {
                throw new RuntimeException("Symbol not found: " + symbol);
            }

            return Stock.builder()
                    .symbol(quote.get("01. symbol"))
                    .price(Double.parseDouble(quote.get("05. price")))
                    .change(Double.parseDouble(quote.get("09. change")))
                    .changePercent(parsePercent(quote.get("10. change percent")))
                    .volume(Long.parseLong(quote.get("06. volume")))
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch stock data", e);
        }
    }


    public List<Double> getHistoricalPrices(String symbol, int days) {
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

        // ← ADD THIS
        System.out.println("History raw response keys: " + response.keySet());

        if (response.containsKey("Note")) {
            throw new RuntimeException("Rate limited: " + response.get("Note"));
        }
        if (response.containsKey("Information")) {
            throw new RuntimeException("API error: " + response.get("Information"));
        }

        Map<String, Map<String, String>> timeSeries =
                (Map<String, Map<String, String>>) response.get("Time Series (Daily)");

        if (timeSeries == null) {
            throw new RuntimeException("No time series data for symbol: " + symbol);
        }

        return timeSeries.values().stream()
                .limit(days)
                .map(day -> Double.parseDouble(day.get("4. close")))
                .toList();
    }



    private double parsePercent(String s){
        return Double.parseDouble(s.replace("%", "").trim());
    }


    public List<StockHistory> getHistory(String symbol, int days){
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

       return timeSeries.entrySet().stream()
               .limit(days)
               .map(entry -> StockHistory.builder()
                       .date(entry.getKey())
                       .open(Double.parseDouble(entry.getValue().get("1. open")))
                       .high(Double.parseDouble(entry.getValue().get("2. high")))
                       .low(Double.parseDouble(entry.getValue().get("3. low")))
                       .close(Double.parseDouble(entry.getValue().get("4. close")))
                       .volume(Long.parseLong(entry.getValue().get("5. volume")))
                       .build())
               .sorted((a,b) -> a.getDate().compareTo(b.getDate())) //oldest -> newest
               .toList();
    }

    // Search stocks by keyword
    public List<StockSearchResult> searchStocks(String keyword){
        WebClient client = webClientBuilder.baseUrl(baseUrl).build();

        Map response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function", "SYMBOL_SEARCH")
                        .queryParam("keywords",keyword)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, String>> matches = (List<Map<String, String>>) response.get("bestMatches");

        if (matches == null) return List.of();

        return matches.stream()
                .map(m -> StockSearchResult.builder()
                        .symbol(m.get("1. symbol"))
                        .name(m.get("2. name"))
                        .type(m.get("3. type"))
                        .region(m.get("4. region"))
                        .currency(m.get("8. currency"))
                        .build())
                .toList();
    }

    private Stock getMockStock(String symbol) {
        return Stock.builder()
                .symbol(symbol)
                .price(3500.00 + (Math.random() * 200))
                .change(Math.random() > 0.5 ? 45.50 : -32.10)
                .changePercent(Math.random() > 0.5 ? 1.32 : -0.91)
                .volume(1200000L)
                .build();
    }








    @Cacheable(value = "topGainers")
    public List<StockMover> getTopGainers() {
        return fetchMovers("day_gainers");
    }

    @Cacheable(value = "topLosers")
    public List<StockMover> getTopLosers() {
        return fetchMovers("day_losers");
    }

    @Cacheable(value = "mostActive")
    public List<StockMover> getMostActive() {
        return fetchMovers("most_actives");
    }

    private List<StockMover> fetchMovers(String screenerType) {
        WebClient client = webClientBuilder
                .baseUrl(yahooBaseUrl)
                .defaultHeader("User-Agent", "Mozilla/5.0")  // required by Yahoo
                .build();

        Map response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/finance/screener/predefined/saved")
                        .queryParam("formatted", "false")
                        .queryParam("scrIds", screenerType)
                        .queryParam("count", "10")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        System.out.println("Yahoo response for " + screenerType + ": " + response);

        return parseMovers(response);
    }

    @SuppressWarnings("unchecked")
    private List<StockMover> parseMovers(Map response) {
        List<StockMover> result = new ArrayList<>();

        try {
            Map finance = (Map) response.get("finance");
            List<Map> results = (List<Map>) finance.get("result");

            if (results == null || results.isEmpty()) return result;

            List<Map> quotes = (List<Map>) results.get(0).get("quotes");

            if (quotes == null) return result;

            for (Map quote : quotes) {
                StockMover mover = StockMover.builder()
                        .symbol(getString(quote, "symbol"))
                        .name(getString(quote, "shortName"))
                        .price(getDouble(quote, "regularMarketPrice"))
                        .change(getDouble(quote, "regularMarketChange"))
                        .changePercent(getDouble(quote, "regularMarketChangePercent"))
                        .volume(getLong(quote, "regularMarketVolume"))
                        .marketCap(getLong(quote, "marketCap"))
                        .build();

                result.add(mover);
            }
        } catch (Exception e) {
            System.err.println("Error parsing movers: " + e.getMessage());
        }

        return result;
    }

    // ── safe getters ──────────────────────────────────────────
    private String getString(Map map, String key) {
        Object val = map.get(key);
        return val != null ? val.toString() : "N/A";
    }

    private double getDouble(Map map, String key) {
        Object val = map.get(key);
        if (val == null) return 0.0;
        try { return Double.parseDouble(val.toString()); }
        catch (Exception e) { return 0.0; }
    }

    private long getLong(Map map, String key) {
        Object val = map.get(key);
        if (val == null) return 0L;
        try { return Long.parseLong(val.toString()); }
        catch (Exception e) { return 0L; }
    }
}
