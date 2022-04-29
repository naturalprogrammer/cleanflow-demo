package com.naturalprogrammer.cleanflow.demo;

import com.naturalprogrammer.cleanflow.CleanFlow;
import com.naturalprogrammer.cleanflow.demo.models.Customer;
import com.naturalprogrammer.cleanflow.demo.models.OrderCreationForm;
import com.naturalprogrammer.cleanflow.demo.models.OrderResource;
import com.naturalprogrammer.cleanflow.demo.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class StatefulOrderCreator {

    private static final Logger log = LoggerFactory.getLogger(StatefulOrderCreator.class);

    private OrderCreationForm orderCreationForm;
    private Customer customer;
    private Product product;
    private boolean paymentSucceeded;
    private OrderResource orderResource;

    public static void main(String[] args) {

        OrderCreationForm form = new OrderCreationForm();
        form.productId = 56;

        new StatefulOrderCreator().createOrder(form);
    }

    public OrderResource createOrder(OrderCreationForm orderCreationForm) {

        log.info("Creating order {}", orderCreationForm);
        this.orderCreationForm = orderCreationForm;
        CleanFlow.execute("create-order.bpmn", this);

        log.info("Created order {}", orderResource);
        return orderResource;
    }

    private void validateForm() {

        if (orderCreationForm.productId == null) {
            log.info("No product chosen");
            throw new IllegalArgumentException("No product chosen");
        }

        customer = getCurrentlyLoggedInCustomer();
        product = getProductById(orderCreationForm.productId);
    }

    private Random random = new Random();

    private String productShippableToCustomerAddress() {
        String shippable = random.nextBoolean() ? "YES" : "NO";
        log.info("Shippable: {}", shippable);
        return shippable;
    }

    private boolean customerHasAnActiveCreditCard() {
        boolean hasCreditCard = random.nextBoolean();
        log.info("Customer has active credit card: {}", hasCreditCard);
        return hasCreditCard;
    }

    private void rejectProductNotShippable() {
        reject("product " + product.id + " is not shippable to your address");
    }

    private void rejectNoActiveCard() {
        reject("Please add an active card");
    }

    private void processPayment() {
        paymentSucceeded = random.nextBoolean();
        log.info("Payment succeeded: {}", paymentSucceeded);
    }

    private String isPaymentSucceeded() {
        String succeeded = paymentSucceeded ? "YES" : "NO";
        log.info("Payment succeeded: {}", succeeded);
        return succeeded;
    }

    private String isCustomerEligibleForCoD() {
        String eligible = random.nextBoolean() ? "YES" : "NO";
        log.info("Eligible for CoD: {}", eligible);
        return eligible;
    }

    private OrderResource createOrderSuccessfully() {

        orderResource  = new OrderResource();
        orderResource.customerId = customer.id;
        orderResource.productId = product.id;
        log.info(orderResource.toString());

        return orderResource;
    }

    private void rejectPaymentProcessingFailed() {
        reject("Payment processing failed");
    }

    private void reject(String message) {
        log.error("Error: {}", message);
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
}

