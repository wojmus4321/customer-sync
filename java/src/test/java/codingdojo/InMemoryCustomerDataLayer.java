package codingdojo;

import java.util.*;

public class InMemoryCustomerDataLayer implements CustomerDataLayer {

    private final Map<String, Customer> customers;
    private final List<ShoppingList> shoppingLists;

    public InMemoryCustomerDataLayer() {
        this.customers = new HashMap<>();
        this.shoppingLists = new ArrayList<>();
    }

    @Override
    public Customer updateCustomerRecord(Customer customer) {
        customers.put(customer.getInternalId(), customer);
        return customer;
    }

    @Override
    public Customer createCustomerRecord(Customer customer) {
        customer.setInternalId(UUID.randomUUID().toString());
        customers.put(customer.getInternalId(), customer);
        return customer;
    }

    @Override
    public void updateShoppingList(ShoppingList consumerShoppingList) {
        shoppingLists.add(consumerShoppingList);
    }

    @Override
    public Customer findByExternalId(String externalId) {
        return customers.values()
                .stream()
                .filter(c -> Objects.equals(c.getExternalId(), externalId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Customer findByMasterExternalId(String externalId) {
        return customers.values()
                .stream()
                .filter(c -> Objects.equals(c.getMasterExternalId(), externalId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Customer findByCompanyNumber(String companyNumber) {
        return customers.values()
                .stream()
                .filter(c -> Objects.equals(c.getCompanyNumber(), companyNumber))
                .findFirst()
                .orElse(null);
    }

    @Override
    public long countCustomers() {
        return customers.size();
    }
}
