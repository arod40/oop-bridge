package store.impl;

import cart.Cart;
import distributor.Distributor;
import distributor.impl.FedEx;
import store.Store;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Walmart implements Store {
	private static Logger LOGGER = Logger.getLogger(Walmart.class.getName());

	private static Map<Long, Order> ordersByOrderId = new HashMap<>();

	private static Long incrementalOrderId = 0l;

	private Distributor distributor;

	@Override
	public Long previewOrder(String toAddr, Cart c) {
		incrementalOrderId++;
		Order order = new Order(incrementalOrderId, c, toAddr);
		ordersByOrderId.put(incrementalOrderId, order);
		Long codeId = distributor.fileOrder("Walmart Address", toAddr, "order-" + incrementalOrderId);
		order.setCodeId(codeId);
		distributor.wrap(codeId);
		describeOrder(order, distributor.cost(codeId));
		return incrementalOrderId;
	}

	private void describeOrder(Order order, BigDecimal distributorQuote){
		LOGGER.log(Level.INFO, order.toString());
		LOGGER.log(Level.INFO, distributorQuote.toString());
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
		distributor.track(ordersByOrderId.get(orderId).getCodeId());
	}

	@Override
	public void setDistributor(Distributor distributor) {
		this.distributor = new FedEx();
	}
}
