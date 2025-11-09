package Order.src;

import java.math.BigDecimal;

public class OrderVo {
    String userId;
    String itemId;
    int qty;
    String coupon;
    boolean vip;
    BigDecimal sum;
    String orderId;

    public OrderVo(String userId, String itemId, int qty, String coupon, boolean vip) {
        this.userId = userId;
        this.itemId = itemId;
        this.qty = qty;
        this.coupon = coupon;
        this.vip = vip;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public boolean getVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}