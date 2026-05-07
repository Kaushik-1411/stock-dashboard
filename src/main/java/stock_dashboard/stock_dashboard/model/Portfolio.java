package stock_dashboard.stock_dashboard.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;

public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String symbol;
    private double buyPrice;
    private int quantity;
    private LocalDateTime addedAt;

//    calculated at runtime, not stored
    @Transient
    private double currentPrice;

    @Transient
    public double getProfitLoss(){
        return (currentPrice - buyPrice) * quantity;
    }

}
