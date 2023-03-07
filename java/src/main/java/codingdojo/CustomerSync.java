package codingdojo;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerSync {

    private final CustomerDataAccess customerDataAccess;

    public CustomerSync(CustomerDataLayer customerDataLayer) {
        this(new CustomerDataAccess(customerDataLayer));
    }

    public CustomerSync(CustomerDataAccess db) {
        this.customerDataAccess = db;
    }

    public boolean syncWithDataLayer(ExternalCustomer externalCustomer) {

        CustomerMatches customerMatches = loadCustomerMatches(externalCustomer);
        Customer customer = customerMatches.getCustomer();

        if (customer == null) {
            customer = Customer.newCustomerWithExternalId(externalCustomer.getExternalId());
        }

        boolean created = customer.getInternalId() == null;
        updateCustomerFields(externalCustomer, customer);
        processDuplicates(externalCustomer, customerMatches);
        customerDataAccess.save(customer);
        return created;
    }

    private CustomerMatches loadCustomerMatches(ExternalCustomer externalCustomer) {
        final String externalId = externalCustomer.getExternalId();
        final String companyNumber = externalCustomer.getCompanyNumber();

        CustomerMatches customerMatches;
        if (externalCustomer.isCompany()) {
            customerMatches = customerDataAccess.loadCompany(externalCustomer);
            if (customerMatches.getCustomer() != null && !CustomerType.COMPANY.equals(customerMatches.getCustomer().getCustomerType())) {
                throw new ConflictException("Existing customer for externalCustomer " + externalId + " already exists and is not a company");
            }

            if (customerMatches.isMatchingByExternalId()) {
                String customerCompanyNumber = customerMatches.getCustomer().getCompanyNumber();
                if (!companyNumber.equals(customerCompanyNumber)) {
                    customerMatches.getCustomer().setMasterExternalId(null);
                    customerMatches.addDuplicate(customerMatches.getCustomer());
                    customerMatches.matchesNone();
                }
            } else if (customerMatches.isMatchingByCompanyNumber()) {
                String customerExternalId = customerMatches.getCustomer().getExternalId();
                if (customerExternalId != null && !externalId.equals(customerExternalId)) {
                    throw new ConflictException("Existing customer for externalCustomer " + companyNumber + " doesn't match external id " + externalId + " instead found " + customerExternalId);
                }
                Customer customer = customerMatches.getCustomer();
                customer.setExternalId(externalId);
                customer.setMasterExternalId(externalId);
                customerMatches.addDuplicate(null);
            }
        } else {
            customerMatches = customerDataAccess.loadPerson(externalCustomer);
            if (customerMatches.getCustomer() != null) {
                if (!CustomerType.PERSON.equals(customerMatches.getCustomer().getCustomerType())) {
                    throw new ConflictException("Existing customer for externalCustomer " + customerMatches + " already exists and is not a person");
                }
            }
        }
        return customerMatches;
    }

    private void processDuplicates(ExternalCustomer externalCustomer, CustomerMatches customerMatches) {
        List<Customer> duplicates = customerMatches.getDuplicates()
                .stream()
                .map(d -> {
                    if (d == null) {
                        Customer duplicate = Customer.newCustomerWithExternalId(externalCustomer.getExternalId());
                        duplicate.setName(externalCustomer.getName());
                        return duplicate;
                    } else {
                        d.setName(externalCustomer.getName());
                        return d;
                    }
                }).collect(Collectors.toList());


        customerDataAccess.save(duplicates);
    }

    private void updateCustomerFields(ExternalCustomer externalCustomer, Customer customer) {
        customer.setName(externalCustomer.getName());
        customer.setAddress(externalCustomer.getPostalAddress());
        customer.setPreferredStore(externalCustomer.getPreferredStore());

        if (externalCustomer.isCompany()) {
            customer.setCompanyNumber(externalCustomer.getCompanyNumber());
            customer.setCustomerType(CustomerType.COMPANY);
        } else {
            customer.setCustomerType(CustomerType.PERSON);
        }

        List<ShoppingList> consumerShoppingLists = externalCustomer.getShoppingLists();
        for (ShoppingList consumerShoppingList : consumerShoppingLists) {
            customer.addShoppingList(consumerShoppingList);
        }
    }


}
