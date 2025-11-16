package Order.src;

class Main {
    public static void main(String[] args) {
        var svc = new OrderService();

        // 정상 케이스
        try {
            var successReq = new CheckoutRequest("vip001 , A-100 , 2 , COUPON10", true);
            String successReceipt = svc.checkout(successReq);
            System.out.println(successReceipt);
        } catch (ValidationException error) {
            System.out.println("ERRORS=" + error.getMessage());
        }

        // 입력 에러 케이스
        try {
            var inputErrorReq = new CheckoutRequest(" ,A-100,-1,", false);
            String inputErrorReceipt = svc.checkout(inputErrorReq);
            System.out.println(inputErrorReceipt);
        } catch (ValidationException error) {
            System.out.println("ERRORS=" + error.getMessage());
        }

        // 재고 부족 케이스
        try {
            var outOfStockErrorReq = new CheckoutRequest("u1,B-100,99, ", false);
            String outOfStockErrorReceipt = svc.checkout(outOfStockErrorReq);
            System.out.println(outOfStockErrorReceipt);
        } catch (ValidationException error) {
            System.out.println("ERRORS=" + error.getMessage());
        }
    }
}