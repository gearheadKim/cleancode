package Order.src;

import java.math.BigDecimal;

public class OrderService {

    public String checkout(CheckoutRequest req) {

        // 검증
        validate(req);

        // 가격 계산
        calculate(req);

        // 할인 적용
        discount(req);

        // 저장
        save(req);

        String receipt = "OK:" + req.getOrderId() + ":" + req.getUserId() + ":" + req.getItemId() + ":"
                + req.getQty() + ":" + req.getFinalOrderAmount();
        System.out.println("[CHECKOUT END] receipt=" + receipt);
        return receipt;
    }

    private void validate(CheckoutRequest req) {
        String userId = req.getUserId();
        if (userId.isEmpty()) {
            throw new ValidationException("USER_REQUIRED");
        }
        String itemId = req.getItemId();
        if (itemId.isEmpty()) {
            throw new ValidationException("ITEM_REQUIRED");
        }

        int qty = req.getQty();
        if (qty <= 0) {
            throw new ValidationException("QTY_POSITIVE");
        }

        // 재고확인
        if (qty > 20) {
            throw new ValidationException("OUT_OF_STOCK");
        }
    }

    private void calculate(CheckoutRequest req) {

        final int BASE_PRICE_TYPE_A = 100;
        final int BASE_PRICE_TYPE_A_OTHER = 150;

        final int SHIPPING_PRICE_TYPE_A = 500;
        final int SHIPPING_PRICE_TYPE_A_OTHER = 1200;

        final int WEIGHT_PER_UNIT = 2; // kg

        String itemId = req.getItemId();

        int basePrice = itemId.startsWith("A") ? BASE_PRICE_TYPE_A : BASE_PRICE_TYPE_A_OTHER;
        int shippingPrice = itemId.startsWith("A") ? SHIPPING_PRICE_TYPE_A : SHIPPING_PRICE_TYPE_A_OTHER;

        int qty = req.getQty();
        int weight = WEIGHT_PER_UNIT * qty;
        int extra = (weight > 10) ? 800 : 0;

        BigDecimal sum = BigDecimal.valueOf(basePrice)
                .multiply(BigDecimal.valueOf(qty))
                .add(BigDecimal.valueOf(shippingPrice))
                .add(BigDecimal.valueOf(extra));

        req.setPrice(sum);
    }

    private void discount(CheckoutRequest req) {

        final Double VIP_DISCOUNT_RATE = 0.9; // 10% 할인
        final BigDecimal COUPON10_DEDUCTION_AMOUNT = BigDecimal.TEN; // 10원 고정 차감

        BigDecimal finalOrderAmount = req.getPrice();
        if (req.getVip()) {
            finalOrderAmount = finalOrderAmount.multiply(BigDecimal.valueOf(VIP_DISCOUNT_RATE));
        }
        if ("COUPON10".equalsIgnoreCase(req.getCoupon())) {
            finalOrderAmount = finalOrderAmount.subtract(COUPON10_DEDUCTION_AMOUNT);
        }

        req.setFinalOrderAmount(finalOrderAmount);
    }

    private void save(CheckoutRequest req) {
        System.out.println("SAVE: order=" + req.getOrderId() + " user=" + req.getUserId() + " item=" + req.getItemId()
                + " qty=" + req.getQty() + " sum=" + req.getFinalOrderAmount());

        req.setLastOrderId(req.getOrderId());
    }
}
