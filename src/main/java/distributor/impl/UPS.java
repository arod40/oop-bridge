package distributor.impl;

import distributor.Distributor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UPS implements Distributor {
    private static BigDecimal wrappingCost = new BigDecimal(10);

    private static BigDecimal pickupCost = new BigDecimal(100);

    private static Map<String, Long> codeIdByCustomerHandle = new HashMap<>();

    private static Map<Long, Order> ordersByCodeId = new HashMap<>();

    private static Map<Long, Set<Service>> servicesByCodeId = new HashMap<>();

    private static Long incrementalCodeId = 0l;

    @Override
    public Long fileOrder(String fromAddr, String toAddr, String customerOrderHandle) {
        incrementalCodeId++;
        Order order = new Order(incrementalCodeId, fromAddr, toAddr, customerOrderHandle);
        codeIdByCustomerHandle.put(customerOrderHandle, incrementalCodeId);
        ordersByCodeId.put(incrementalCodeId, order);
        servicesByCodeId.put(incrementalCodeId, new HashSet<>());
        return order.getCodeId();
    }

    @Override
    public void wrap(Long codeId) {
        if (!servicesByCodeId.get(codeId).contains(Service.WRAP)){
            servicesByCodeId.get(codeId).add(Service.WRAP);
            ordersByCodeId.get(codeId).addCost(wrappingCost);
        }
    }

    @Override
    public void pickupService(Long codeId) {
        if (!servicesByCodeId.get(codeId).contains(Service.PICKUP)){
            ordersByCodeId.get(codeId).addCost(pickupCost);
            servicesByCodeId.get(codeId).add(Service.PICKUP);
        }
    }

    @Override
    public void dropAtBranch(Long codeId) {
        if (servicesByCodeId.get(codeId).contains(Service.PICKUP)){
            ordersByCodeId.get(codeId).substractCost(pickupCost);
            servicesByCodeId.get(codeId).remove(Service.PICKUP);
        }

    }

    @Override
    public BigDecimal cost(Long codeId) {
        return ordersByCodeId.get(codeId).getCost();
    }

    @Override
    public void pay(Long codeId) {
        ordersByCodeId.get(codeId).setIsPayed(true);
    }

    @Override
    public void route(Long codeId) {
        ordersByCodeId.get(codeId).setIsShipped(true);
    }

    @Override
    public Long lookupCustomerHandle(String customerOrderHandle) {
        return codeIdByCustomerHandle.get(customerOrderHandle);
    }

    @Override
    public void cancel(Long codeId) {
        ordersByCodeId.get(codeId).setIsCanceled(true);
    }

    @Override
    public void confirmDelivery(Long codeId) {
        ordersByCodeId.get(codeId).setIsDelivered(true);
    }

    @Override
    public void track(Long codeId) {
        System.out.println("=== UPS Info. ===");

        if (!ordersByCodeId.containsKey(codeId)){
            System.out.println("    There is no record of an order with id: " + codeId);
        }
        else{
            Order order = ordersByCodeId.get(codeId);

            if (order.getIsCanceled()) {
                System.out.println("    Order was CANCELED");
            }
            else if(!order.getIsPayed()){
                System.out.println("    Order is NOT CONFIRMED yet");
            }
            else if (!order.getIsShipped()){
                System.out.println("    Order is at "+ order.getFromAddr());
            }
            else if (!order.getIsDelivered()){
                System.out.println("    Order was shipped to "+ order.getToAddr());
            }
            else{
                System.out.println("    Order was delivered to "+ order.getToAddr());
            }
        }
    }

    @Override
    public boolean isOrderShipped(Long codeId) {
        return ordersByCodeId.get(codeId).getIsShipped();
    }

    @Override
    public boolean isOrderCanceled(Long codeId) {
        return ordersByCodeId.get(codeId).getIsCanceled();
    }
}
