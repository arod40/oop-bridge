package cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import product.Product;

public class Cart {

	private List<CartLineItem> orderList = new ArrayList<>();

	/**
	 * Add a product to the cart
	 * @param product
	 * @param quantity
	 */
	public Cart addLine(Product product, int quantity) {
		for (CartLineItem line : orderList) {
			if (line.getProduct() == product) {
				line.setQuantity(line.getQuantity() + quantity);
				return this;
			}
		}

		orderList.add(CartLineItem.make(product, quantity));
		return this;
	}
	
	/**
	 * @return The total cart cost
	 */
	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for (CartLineItem orderLine : orderList) {
			total = total.add(orderLine.getSubTotal());
		}
		return total;
	}

	/**
	 * @return get all items as a list
	 */
	public List<CartLineItem> getOrderList() {
		return orderList;
	}
}
