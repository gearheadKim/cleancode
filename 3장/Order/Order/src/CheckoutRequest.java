package Order.src;

import java.math.BigDecimal;
import java.util.UUID;

public class CheckoutRequest {
    String raw;
    String userId;
    String itemId;
    int qty;
    String coupon;
    boolean vip;
    BigDecimal price;
    BigDecimal finalOrderAmount;
    String orderId;
    String lastOrderId;

    public CheckoutRequest(String raw, boolean vip) {
        System.out.println("[CHECKOUT START] raw=" + raw + " vip=" + vip);
        this.parseOrderRequest(raw, vip);
        this.orderId = UUID.randomUUID().toString();
    }

    private void parseOrderRequest(String raw, boolean vip) {
        // raw: "userId , itemId , qty , coupon"
        String[] parts = raw.split(",");
        if (parts.length < 4) {
            throw new ValidationException("INVALID_FORMAT");
        }

        this.userId = parts[0].trim();
        this.itemId = parts[1].trim();

        int qty = 0;
        try {
            qty = Integer.parseInt(parts[2].trim());
        } catch (Exception ex) {
            throw new ValidationException("QTY_NOT_NUMBER");
        }
        this.qty = qty;
        this.coupon = (parts.length >= 4) ? parts[3].trim() : "";
        this.vip = vip;
    }

    public String getRaw() {
        return raw;
    }

    public String getUserId() {
        return userId;
    }

    public String getItemId() {
        return itemId;
    }

    public int getQty() {
        return qty;
    }

    public String getCoupon() {
        return coupon;
    }

    public boolean getVip() {
        return vip;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFinalOrderAmount() {
        return finalOrderAmount;
    }

    public void setFinalOrderAmount(BigDecimal finalOrderAmount) {
        this.finalOrderAmount = finalOrderAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getLastOrderId() {
        return lastOrderId;
    }

    public void setLastOrderId(String lastOrderId) {
        this.lastOrderId = lastOrderId;
    }
}
