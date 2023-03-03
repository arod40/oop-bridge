import cart.Cart;
import com.sun.org.apache.xpath.internal.operations.Or;
import distributor.Distributor;
import distributor.impl.FedEx;
import distributor.impl.UPS;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import product.Product;
import store.Store;
import store.impl.Amazon;
import store.impl.Walmart;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BridgeTests {
    @Test
    public void happyPath() {
        Cart order = makeOrder();

        showHappyPath(new Amazon(), new UPS(), order);
        System.out.println("=========================================================\n");
        showHappyPath(new Amazon(), new FedEx(), order);
        System.out.println("=========================================================\n");
        showHappyPath(new Walmart(), new UPS(), order);
        System.out.println("=========================================================\n");
        showHappyPath(new Walmart(), new FedEx(), order);
        System.out.println("=========================================================\n");
    }


    @Test
    public void cancelSuccess(){
        Store store = new Amazon();
        Distributor distributor = new UPS();
        Cart order = makeOrder();

        store.setDistributor(distributor);

        System.out.println("\nCreating and order");
        Long orderId = store.previewOrder("Baylor University", order);

        System.out.println("\nProcessing the order");
        store.processOrder(orderId);

        System.out.println("\nTracking info after processing");
        store.tracking(orderId);


        Long codeId = distributor.lookupCustomerHandle(store.getHandleOfOrder(orderId));

        System.out.println("\nCanceling the order");
        store.cancel(orderId);

        assertTrue(store.isOrderCanceled(orderId));
        assertTrue(distributor.isOrderCanceled(codeId));

        System.out.println("\nTracking info after canceling");
        store.tracking(orderId);
        System.out.println("=========================================================\n");
    }

    @Test
    public void cancelFailure(){
        Store store = new Amazon();
        Distributor distributor = new UPS();
        Cart order = makeOrder();

        store.setDistributor(distributor);

        System.out.println("\nCreating and order");
        Long orderId = store.previewOrder("Baylor University", order);

        System.out.println("\nProcessing the order");
        store.processOrder(orderId);

        System.out.println("\nTracking info after processing");
        store.tracking(orderId);

        Long codeId = distributor.lookupCustomerHandle(store.getHandleOfOrder(orderId));

        System.out.println("\nShipping the order");
        distributor.route(codeId);

        System.out.println("\nTracking info after shipping");
        store.tracking(codeId);

        System.out.println("\nAttempting to cancel the order after it was shipped");
        store.cancel(orderId);

        assertFalse(store.isOrderCanceled(orderId));
        assertFalse(distributor.isOrderCanceled(codeId));

        System.out.println("\nTracking info after canceling attempt");
        store.tracking(orderId);
        System.out.println("=========================================================\n");
    }

    private void showHappyPath(Store store, Distributor distributor, Cart order){
        store.setDistributor(distributor);

        System.out.println("\nCreating and order");
        Long orderId = store.previewOrder("Baylor University", order);

        System.out.println("\nProcessing the order");
        store.processOrder(orderId);

        System.out.println("\nTracking info after processing");
        store.tracking(orderId);

        Long codeId = distributor.lookupCustomerHandle(store.getHandleOfOrder(orderId));

        System.out.println("\nShipping the order");
        distributor.route(codeId);

        System.out.println("\nTracking info after shipping");
        store.tracking(codeId);

        System.out.println("\nConfirming delivery");
        distributor.confirmDelivery(codeId);

        System.out.println("\nTracking info after delivery");
        store.tracking(codeId);
    }

    private Cart makeOrder(){
        Product soap = Product.make("Soap").init("Nice protocol", new BigDecimal(30));
        Product tabaco = Product.make("Tabaco").init("Dont smoke", new BigDecimal(20));
        Product book = Product.make("Book").init("Read me", new BigDecimal(25));
        Product lego = Product.make("Lego").init("Play me", new BigDecimal(35));

        Cart order = new Cart();
        order.addLine(soap, 5).addLine(tabaco, 2).addLine(book, 1).addLine(lego, 1);

        return order;
    }

}
