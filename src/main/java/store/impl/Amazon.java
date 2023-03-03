package store.impl;

import cart.Cart;
import cart.CartLineItem;
import distributor.Distributor;
import distributor.impl.FedEx;
import store.Store;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Amazon implements Store {
    private static Logger LOGGER = Logger.getLogger(Amazon.class.getName());

    private static Map<Long, Store.Order> ordersByOrderId = new HashMap<>();

    private static Long incrementalOrderId = 0l;

    private Distributor distributor;

    @Override
    public Long previewOrder(String toAddr, Cart c) {
        incrementalOrderId++;
        Order order = new Order(incrementalOrderId, c, toAddr);
        ordersByOrderId.put(incrementalOrderId, order);
        Long codeId = distributor.fileOrder("Amazon Address", toAddr, "order-" + incrementalOrderId);
        order.setCodeId(codeId);
        distributor.pickupService(codeId);
        _describeOrder(order, distributor.cost(codeId));
        return incrementalOrderId;
    }

    private void _describeOrder(Order order, BigDecimal distributorQuote){
        LOGGER.log(Level.INFO, "=== Order Summary ===");
        LOGGER.log(Level.INFO, "Items: ITEM NAME --------- QTY -------- PRICE");
        for (CartLineItem cli: order.getCart().getOrderList()){
            LOGGER.log(Level.INFO, cli.getProduct().getName() + " --------- " + cli.getQuantity() + " -------- " + cli.getSubTotal());
        }
        LOGGER.log(Level.INFO, "----------------------------------------------");
        LOGGER.log(Level.INFO, "Delivery Service:", distributorQuote.toString());
        LOGGER.log(Level.INFO, "TOTAL (goods + delivery):", order.getCart().getTotal().add(distributorQuote));
    }

    @Override
    public void processOrder(Long orderId) {
        Order order = ordersByOrderId.get(orderId);
        order.setIsPayed(true);
        distributor.pay(order.getCodeId());
    }

    @Override
    public void cancel(Long orderId) {
        Order order = ordersByOrderId.get(orderId);
        if (!distributor.isOrderShipped(order.getCodeId())){
            order.setIsCanceled(true);
            distributor.cancel(order.getCodeId());
        }
    }

    @Override
    public void tracking(Long orderId) {
        LOGGER.log(Level.INFO, "=== Amazon Info. ===");

        if (!ordersByOrderId.containsKey(orderId)){
            LOGGER.log(Level.INFO, "    There is no record of an order with id: " + orderId);
        }
        else{
            Order order = ordersByOrderId.get(orderId);

            if (order.getIsCanceled()) {
                LOGGER.log(Level.INFO, "    Order was CANCELED");
            }
            else if (!order.getIsPayed()){
                LOGGER.log(Level.INFO, "    Order was CREATED (but has not been processed yet)");
            }
            else{
                LOGGER.log(Level.INFO, "    Order was PROCESSED and sent to the distributor");
                distributor.track(ordersByOrderId.get(orderId).getCodeId());
            }
        }
    }

    @Override
    public void setDistributor(Distributor distributor) {
        this.distributor = new FedEx();
    }
}
