package stock_dashboard.stock_dashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "portfolio")
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
