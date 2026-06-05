package stock_dashboard.stock_dashboard.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockMover {
    private String symbol;
    private String name;
    private double price;
    private double change;
    private double changePercent;
    private long volume;
    private long marketCap;
}
