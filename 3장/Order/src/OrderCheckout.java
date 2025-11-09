package Order.src;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * 문제 의도: 한 함수에서 너무 많은 일을 함
 * - 플래그 인자(vip)
 * - 출력 인자(errors)
 * - 매직 넘버와 문자열
 * - 파싱/검증/가격/재고/할인/저장/영수증조립/로깅을 한 함수에서 처리
 * - 부작용(내부 상태 lastOrderId)
 * - 예외 대신 오류코드/문자열로 제어
 */
class OrderCheckout {
    private String lastOrderId; // 사이드 이펙트 예시

    /**
     * raw: "userId , itemId , qty , coupon"
     * vip: VIP 여부(플래그 인자)
     * errors: 오류 메시지를 채워 넣는 출력 인자
     */
    public String checkout(String raw, boolean vip, List<String> errors) {
        // 로깅(임시)
        System.out.println("[CHECKOUT START] raw=" + raw + " vip=" + vip);

        // 파싱
        String[] parts = raw.split(",");
        if (parts.length < 3) {
            errors.add("INVALID_FORMAT");
            return "ERROR";
        }
        String userId = parts[0].trim();
        String itemId = parts[1].trim();
        String qtyStr = parts[2].trim();
        String coupon = (parts.length >= 4) ? parts[3].trim() : "";

        // 검증
        if (userId.isEmpty())
            errors.add("USER_REQUIRED");
        if (itemId.isEmpty())
            errors.add("ITEM_REQUIRED");
        int qty = 0;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (Exception ex) {
            errors.add("QTY_NOT_NUMBER");
        }
        if (qty <= 0)
            errors.add("QTY_POSITIVE");

        if (!errors.isEmpty())
            return "ERROR";

        // 가격(매직넘버 다수)
        int baseUnit = itemId.startsWith("A") ? 100 : 150; // A면 100원, 아니면 150원
        int shipping = itemId.startsWith("A") ? 500 : 1200; // KR 가정
        int weightPerUnit = 2; // kg
        int weight = weightPerUnit * qty;
        int extra = (weight > 10) ? 800 : 0;

        BigDecimal sum = BigDecimal.valueOf(baseUnit)
                .multiply(BigDecimal.valueOf(qty))
                .add(BigDecimal.valueOf(shipping))
                .add(BigDecimal.valueOf(extra));

        // VIP/쿠폰 할인(분기 중첩 + 플래그 인자)
        if (vip) {
            sum = sum.multiply(BigDecimal.valueOf(0.9)); // 10% 할인
        }
        if ("COUPON10".equalsIgnoreCase(coupon)) {
            sum = sum.subtract(BigDecimal.TEN); // 10원 고정 차감
        }

        // 재고 확인(임시)
        if (qty > 20) {
            errors.add("OUT_OF_STOCK");
            return "ERROR";
        }

        // 저장(모킹) + 사이드이펙트
        String orderId = UUID.randomUUID().toString();
        System.out.println(
                "SAVE: order=" + orderId + " user=" + userId + " item=" + itemId + " qty=" + qty + " sum=" + sum);
        lastOrderId = orderId;

        // 영수증 문자열 조립
        String receipt = "OK:" + orderId + ":" + userId + ":" + itemId + ":" + qty + ":" + sum;
        System.out.println("[CHECKOUT END] receipt=" + receipt);
        return receipt;
    }

    public String getLastOrderId() {
        return lastOrderId;
    }
}