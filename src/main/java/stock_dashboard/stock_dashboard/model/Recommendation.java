package stock_dashboard.stock_dashboard.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Recommendation {

    private String symbol;
    private String signal;       // "BUY" | "SELL" | "HOLD"
    private String reason;
    private double rsi;
    private double shortMA;
    private double longMA;
}
