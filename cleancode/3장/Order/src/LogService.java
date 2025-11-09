package Order.src;

public class LogService {

    public void firstLog(String raw, boolean vip) {
        System.out.println("[CHECKOUT START] raw=" + raw + " vip=" + vip);
    }

    public void errorLog() {
        System.out.println("ERROR");
    }

    public void saveLog(OrderVo orderVo) {
        System.out.println("SAVE: order=" + orderVo.getOrderId() + " user=" + orderVo.getUserId() + " item="
                + orderVo.getItemId() + " qty=" + orderVo.getQty() + " sum=" + orderVo.getSum());
    }

    public void receiptLog(OrderVo orderVo) {
        String receipt = "OK:" + orderVo.getOrderId() + ":" + orderVo.getUserId() + ":" + orderVo.getItemId() + ":"
                + orderVo.getQty() + ":" + orderVo.getSum();
        System.out.println("[CHECKOUT END] receipt=" + receipt);
    }
}
