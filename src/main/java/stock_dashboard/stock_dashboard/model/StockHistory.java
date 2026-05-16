package stock_dashboard.stock_dashboard.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockHistory {
    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
}
