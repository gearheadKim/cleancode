package Order.src;

import java.util.*;

class Main {
    public static void main(String[] args) {

        /* as-is */
        // OrderCheckout svc = new OrderCheckout();

        // 정상 케이스
        // List<String> errors = new ArrayList<>();
        // String receipt = svc.checkout("vip001 , A-100 , 2 , COUPON10", true, errors);
        // System.out.println("RECEIPT=" + receipt);
        // System.out.println("ERRORS=" + errors);

        // 입력 에러 케이스
        // List<String> e2 = new ArrayList<>();
        // System.out.println("BAD=" + svc.checkout(" ,A-100,-1,", false, e2));
        // System.out.println("ERRORS2=" + e2);

        /* to-be */

        OrderService orderService = new OrderService();

        List<String> error;
        // 정상 케이스
        error = new ArrayList<>();
        String successReceipt = orderService.checkout("vip001 , A-100 , 2 , COUPON10", true, error);
        System.out.println("RECEIPT=" + successReceipt);
        System.out.println("ERRORS=" + error);

        // 입력 에러 케이스
        error = new ArrayList<>();
        String inputErrorReceipt = orderService.checkout(" ,A-100,-1,", false, error);
        System.out.println("RECEIPT=" + inputErrorReceipt);
        System.out.println("ERRORS=" + error);

    }
}