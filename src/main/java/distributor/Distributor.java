package distributor;

import java.math.BigDecimal;

public interface Distributor {
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
}
