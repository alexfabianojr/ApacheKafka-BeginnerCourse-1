package ecommerce.entidade;

import java.math.BigDecimal;

public class Order {
    private final String userId;
    private final String oderId;
    private final BigDecimal amount;

    public Order(String userId, String oderId, BigDecimal amount) {
        this.userId = userId;
        this.oderId = oderId;
        this.amount = amount;
    }
}
