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
		distributor.track(ordersByOrderId.get(orderId).getCodeId());
	}

	@Override
	public void setDistributor(Distributor distributor) {
		this.distributor = new FedEx();
	}
}
