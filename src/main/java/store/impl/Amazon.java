package store.impl;

import cart.Cart;
import cart.CartLineItem;
import distributor.Distributor;
import store.Store;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Amazon implements Store {
    private static Map<Long, Store.Order> ordersByOrderId = new HashMap<>();

    private static Long incrementalOrderId = 0l;

    private Distributor distributor;

    @Override
    public Long previewOrder(String toAddr, Cart c) {
        incrementalOrderId++;
        Order order = new Order(incrementalOrderId, c, toAddr);
        ordersByOrderId.put(incrementalOrderId, order);
        Long codeId = distributor.fileOrder("Amazon Address", toAddr, "AmazonOrder-" + incrementalOrderId);
        order.setCodeId(codeId);
        distributor.pickupService(codeId);
        _describeOrder(order, distributor.cost(codeId));
        return incrementalOrderId;
    }

    private void _describeOrder(Order order, BigDecimal distributorQuote){
        System.out.println("=== Order Summary ===");
        System.out.println("Items: ITEM NAME --------- QTY -------- PRICE");
        for (CartLineItem cli: order.getCart().getOrderList()){
            System.out.println(cli.getProduct().getName() + " --------- " + cli.getQuantity() + " -------- " + cli.getSubTotal());
        }
        System.out.println("----------------------------------------------");
        System.out.println("Delivery Service:" + distributorQuote.toString());
        System.out.println("TOTAL (goods + delivery):" + order.getCart().getTotal().add(distributorQuote));
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
        System.out.println("=== Amazon Info. ===");

        if (!ordersByOrderId.containsKey(orderId)){
            System.out.println("    There is no record of an order with id: " + orderId);
        }
        else{
            Order order = ordersByOrderId.get(orderId);

            if (order.getIsCanceled()) {
                System.out.println("    Order was CANCELED");
            }
            else if (!order.getIsPayed()){
                System.out.println("    Order was CREATED (but has not been processed yet)");
            }
            else{
                System.out.println("    Order was PROCESSED and sent to the distributor");
                distributor.track(ordersByOrderId.get(orderId).getCodeId());
            }
        }
    }

    @Override
    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    @Override
    public String getHandleOfOrder(Long orderId) {
        return "AmazonOrder-" + orderId;
    }

    @Override
    public boolean isOrderCanceled(Long orderId) {
        return ordersByOrderId.get(orderId).getIsCanceled();
    }
}
