package com.naturalprogrammer.cleanflow.demo;

import com.naturalprogrammer.cleanflow.CleanFlow;
import com.naturalprogrammer.cleanflow.Returns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class OrderCreationService {

    private static final Logger log = LoggerFactory.getLogger(OrderCreationService.class);

    public OrderResource createOrder(OrderCreationForm orderCreationForm) {

        log.info("Creating order {}", orderCreationForm);
        OrderResource resource = (OrderResource) CleanFlow
                .execute("create-order.bpmn", this,
                        Map.of("orderCreationForm", orderCreationForm))
                .get("orderResource");

        log.info("Created order {}", resource);
        return resource;
    }

    @Returns({"customer", "product"})
    private List<?> validateForm(OrderCreationForm orderCreationForm) {

        log.info("Validating {}", orderCreationForm);

        if (orderCreationForm.productId == null) {
            log.info("No product chosen");
            throw new IllegalArgumentException("No product chosen");
        }

        Customer customer = getCurrentlyLoggedInCustomer();
        Product product = getProductById(orderCreationForm.productId);

        return List.of(customer, product);
    }

    private Customer getCurrentlyLoggedInCustomer() {

        Customer customer = new Customer();
        customer.id = 34;
        log.info("Logged in customer: {} ", customer);

        return customer;
    }

    private Product getProductById(Integer productId) {

        Product product = new Product();
        product.id = productId;
        return product;
    }

    private Random random = new Random();

    private String productShippableToCustomerAddress(Customer customer, Product product) {
        String shippable = random.nextBoolean() ? "YES" : "NO";
        log.info("Shippable: {}", shippable);
        return shippable;
    }

    private String customerHasAnActiveCreditCard(Customer customer) {
        String hasCreditCard = random.nextBoolean() ? "YES" : "NO";
        log.info("Customer has active credit card: {}", hasCreditCard);
        return hasCreditCard;
    }

    private void rejectProductNotShippable(Product product) {
        reject("product " + product.id + " is not shippable to your address");
    }

    private void rejectNoActiveCard(Product product) {
        reject("Please add an active card");
    }

    @Returns("paymentSucceeded")
    private boolean processPayment(Customer customer, Product product) {
        boolean succeeded = random.nextBoolean();
        log.info("Payment succeeded: {}", succeeded);
        return succeeded;
    }

    private String isPaymentSucceeded(boolean paymentSucceeded) {
        String succeeded = paymentSucceeded ? "YES" : "NO";
        log.info("Payment succeeded: {}", succeeded);
        return succeeded;
    }

    private String isCustomerEligibleForCoD(Customer customer) {
        String eligible = random.nextBoolean() ? "YES" : "NO";
        log.info("Eligible for CoD: {}", eligible);
        return eligible;
    }

    @Returns("orderResource")
    private OrderResource createOrderSuccessfully(Customer customer, Product product) {

        OrderResource resource = new OrderResource();
        resource.customerId = customer.id;
        resource.productId = product.id;
        log.info(resource.toString());

        return resource;
    }

    private void rejectPaymentProcessingFailed() {
        reject("Payment processing failed");
    }

    private void reject(String message) {
        log.error("Error: {}", message);
    }

    public static void main(String[] args) {

        OrderCreationForm form = new OrderCreationForm();
        form.productId = 56;

        new OrderCreationService().createOrder(form);
    }
}

class OrderCreationForm {

    public Integer productId;

    @Override
    public String toString() {
        return "OrderCreationForm{" +
                "productId=" + productId +
                '}';
    }
}

class OrderResource {

    public Integer customerId;
    public Integer productId;

    @Override
    public String toString() {
        return "OrderResource{" +
                "customerId=" + customerId +
                ", productId=" + productId +
                '}';
    }
}

class Product {

    public Integer id;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                '}';
    }
}

class Customer {

    public Integer id;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                '}';
    }
}
