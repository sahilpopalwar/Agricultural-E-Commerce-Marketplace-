package org.project.oopjava.delivery;

public class DeliveryPublishException extends RuntimeException {
    public DeliveryPublishException() {
        super("Delivery update could not be published");
    }
}
