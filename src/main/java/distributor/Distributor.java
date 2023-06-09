package distributor;

import lombok.Data;

import java.math.BigDecimal;

public interface Distributor {

	@Data
	class Order {
		Long codeId;
		String fromAddr;
		String toAddr;
		String customerHandle;
		BigDecimal cost;

		Boolean isPayed = false;
		Boolean isShipped = false;
		Boolean isDelivered = false;
		Boolean isCanceled = false;

		public Order(Long codeId, String fromAddr, String toAddr, String customerHandle){
			this.codeId = codeId;
			this.fromAddr = fromAddr;
			this.toAddr = toAddr;
			this.customerHandle = customerHandle;
			this.cost = new BigDecimal(0);
		}

		public void addCost(BigDecimal cost) {
			this.cost = this.cost.add(cost);
		}

		public void substractCost(BigDecimal cost) {
			this.cost = this.cost.subtract(cost);
		}
	}

	enum Service{
		WRAP,
		PICKUP,
	}

	Long fileOrder(String fromAddr, String toAddr, String customerOrderHandle);

	void wrap(Long codeId);

	void pickupService(Long codeId);

	void dropAtBranch(Long codeId);

	BigDecimal cost(Long codeId);

	void pay(Long codeId);

	void route(Long codeId);

	Long lookupCustomerHandle(String customerOrderHandle);

	void cancel(Long codeId);

	void confirmDelivery(Long codeId);

	void track(Long codeId);

	boolean isOrderShipped(Long codeId);

	// For testig purposes only
	boolean isOrderCanceled(Long codeId);
}
