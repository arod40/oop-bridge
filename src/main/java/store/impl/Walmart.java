package store.impl;

import cart.Cart;
import distributor.Distributor;
import store.Store;

public class Walmart implements Store {
	@Override
	public Long previewOrder(String toAddr, Cart c) {
		return null;
	}

	@Override
	public void processOrder(Long orderId) {

	}

	@Override
	public void cancel(Long orderId) {

	}

	@Override
	public void tracking(Long orderId) {

	}

	@Override
	public void setDistributor(Distributor distributor) {

	}
}
