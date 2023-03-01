package distributor.impl;

import distributor.Distributor;

import java.math.BigDecimal;

public class FedEx implements Distributor {
    @Override
    public Long fileOrder(String fromAddr, String toAddr, String customerOrderHandle) {
        return null;
    }

    @Override
    public void wrap(Long codeId) {

    }

    @Override
    public void pickupService(Long codeId) {

    }

    @Override
    public void dropAtBranch(Long codeId) {

    }

    @Override
    public BigDecimal cost(Long codeId) {
        return null;
    }

    @Override
    public void pay(Long codeId) {

    }

    @Override
    public void route(Long codeId) {

    }

    @Override
    public Long lookupCustomerHandle(String customerOrderHandle) {
        return null;
    }

    @Override
    public void cancel(Long codeId) {

    }

    @Override
    public void confirmDelivery(Long codeId) {

    }

    @Override
    public void track(Long codeId) {

    }
}
