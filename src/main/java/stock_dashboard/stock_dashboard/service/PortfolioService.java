package stock_dashboard.stock_dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stock_dashboard.stock_dashboard.model.Portfolio;
import stock_dashboard.stock_dashboard.model.Stock;
import stock_dashboard.stock_dashboard.repository.PortfolioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final StockService stockService;

    public List<Portfolio> getPortfolio(String userId){
        List<Portfolio> holdings = portfolioRepository.findByUserId(userId);

        holdings.forEach(holding -> {
            try{
                Stock live = stockService.getStockQuote(holding.getSymbol());
                holding.setCurrentPrice(live.getPrice());
            } catch (Exception e){
                // If API fails, currentPrice stays 0 - dont crash the whole list
                holding.setCurrentPrice(holding.getBuyPrice());
            }
        });

        return holdings;
    }

    public Portfolio addStock(Portfolio portfolio){
        portfolio.setAddedAt(LocalDateTime.now());
        return portfolioRepository.save(portfolio);
    }

    public void removeStock(Long id){
         portfolioRepository.deleteById(id);
    }

    public PortfolioSummary getSummary(String userId){
        List<Portfolio> holdings = getPortfolio(userId);

        double totalInvested = holdings.stream()
                .mapToDouble(h -> h.getBuyPrice() * h.getQantity())
                .sum();

        double currentValue = holdings.stream()
                .mapToDouble(h -> h.getCurrentPrice() * h.getQuantity())
                .sum();

        return new PortfolioSummary(totalInvested, currentValue, currentValue - totalInvested);
    }

    // Inner record — no need for a separate file
    public record PortfolioSummary(
            double totalInvested;
            double currentValue;
            double profitLoss;
    ) {}




}
