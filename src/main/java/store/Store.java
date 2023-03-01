package store;

import java.util.ArrayList;
import java.util.List;

import cart.Cart;
import distributor.Distributor;

public interface Store {
	Long previewOrder(String toAddr, Cart c);

	void processOrder(Long orderId);

	void cancel(Long orderId);

	void tracking(Long orderId);

	void setDistributor(Distributor distributor);
}
