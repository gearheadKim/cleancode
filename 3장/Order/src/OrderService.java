package Order.src;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderService {

    LogService logService;

    public String checkout(String raw, boolean vip, List<String> errors) {
        logService = new LogService();

        // 로깅
        logService.firstLog(raw, vip);

        // 파싱
        OrderVo orderVo = createOrderVo(raw, vip, errors);

        // 검증
        if (!isValid(orderVo, errors)) {
            logService.errorLog();
        }

        // 가격(매직넘버 다수)
        BigDecimal sum = price(orderVo);
        orderVo.setSum(sum);

        // 재고 확인(임시)
        if (orderVo.getQty() > 20) {
            errors.add("OUT_OF_STOCK");
            logService.errorLog();
        }

        // 저장(모킹) + 사이드이펙트
        String orderId = UUID.randomUUID().toString();
        orderVo.setOrderId(orderId);
        logService.saveLog(orderVo);

        // String lastOrderId = orderId;
        logService.receiptLog(orderVo);

        String receipt = "OK:" + orderVo.getOrderId() + ":" + orderVo.getUserId() + ":" + orderVo.getItemId() + ":"
                + orderVo.getQty() + ":" + orderVo.getSum();
        return receipt;
    }

    public OrderVo createOrderVo(String raw, boolean vip, List<String> errors) {
        // raw: "userId , itemId , qty , coupon"
        String[] parts = raw.split(",");
        if (parts.length < 3) {
            errors.add("INVALID_FORMAT");
            logService.errorLog();
        }

        String userId = parts[0].trim();
        String itemId = parts[1].trim();
        String qtyStr = parts[2].trim();
        String coupon = (parts.length >= 4) ? parts[3].trim() : "";

        int qty = 0;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (Exception ex) {
            errors.add("QTY_NOT_NUMBER");
            logService.errorLog();
        }

        return new OrderVo(userId, itemId, qty, coupon, vip);
    }

    public boolean isValid(OrderVo orderVo, List<String> errors) {

        String userId = orderVo.getUserId();
        if (userId.isEmpty()) {
            errors.add("USER_REQUIRED");
            logService.errorLog();
            return false;
        }
        String itemId = orderVo.getItemId();
        if (itemId.isEmpty()) {
            errors.add("ITEM_REQUIRED");
            logService.errorLog();
        }

        int qty = orderVo.getQty();
        if (qty <= 0) {
            errors.add("QTY_POSITIVE");
            logService.errorLog();
        }

        return true;
    }

    public BigDecimal price(OrderVo orderVo) {

        String itemId = orderVo.getItemId();

        boolean isBaseUnit = itemId.startsWith("A");
        int baseUnit = 150;
        if (isBaseUnit) {
            baseUnit = 100;
        }

        boolean isShipping = itemId.startsWith("A");
        int shipping = 1200;
        if (isShipping) {
            shipping = 500;
        }

        int weightPerUnit = 2; // kg
        int qty = orderVo.getQty();
        int weight = weightPerUnit * qty;
        int extra = (weight > 10) ? 800 : 0;

        BigDecimal sum = BigDecimal.valueOf(baseUnit)
                .multiply(BigDecimal.valueOf(qty))
                .add(BigDecimal.valueOf(shipping))
                .add(BigDecimal.valueOf(extra));

        if (orderVo.getVip()) {
            sum = sum.multiply(BigDecimal.valueOf(0.9)); // 10% 할인
        }
        if ("COUPON10".equalsIgnoreCase(orderVo.getCoupon())) {
            sum = sum.subtract(BigDecimal.TEN); // 10원 고정 차감
        }

        return sum;
    }
}
