package store;

import cart.Cart;
import distributor.Distributor;
import lombok.Data;

public interface Store {
	@Data
	class Order{
		Long orderId;

		Long codeId;

		Cart cart;

		String toAddr;

		Boolean isPayed = false;
		Boolean isCanceled = false;

		public Order(Long orderId, Cart cart, String toAddr){
			this.orderId = orderId;
			this.cart = cart;
			this.toAddr = toAddr;
		}
	}
	Long previewOrder(String toAddr, Cart c);

	void processOrder(Long orderId);

	void cancel(Long orderId);

	void tracking(Long orderId);

	void setDistributor(Distributor distributor);
}
