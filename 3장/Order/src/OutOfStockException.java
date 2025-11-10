package Order.src;

public class OutOfStockException extends RuntimeException {
    OutOfStockException(String message) {
        super(message);
    }
}
