package stock_dashboard.stock_dashboard.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockSearchResult {
    private String symbol;
    private String name;
    private String type;
    private String region;
    private String currency;
}
