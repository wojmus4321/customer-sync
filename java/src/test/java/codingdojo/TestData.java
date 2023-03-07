package codingdojo;

import java.util.Collections;
import java.util.UUID;

public class TestData {

    static ExternalCustomer anExternalPerson() {
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(UUID.randomUUID().toString());
        externalCustomer.setName("John Doe");
        externalCustomer.setAddress(new Address("123 main st", "Helsingborg", "SE-123 45"));
        externalCustomer.setCompanyNumber(null);
        externalCustomer.setShoppingLists(Collections.singletonList(new ShoppingList("lipstick", "blusher")));
        externalCustomer.setPreferredStore("Test Store 123");
        externalCustomer.setBonusPointsBalance(100);
        return externalCustomer;
    }

    static ExternalCustomer anExternalCompany() {
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(UUID.randomUUID().toString());
        externalCustomer.setName("Acme Inc.");
        externalCustomer.setAddress(new Address("123 main st", "Helsingborg", "SE-123 45"));
        externalCustomer.setCompanyNumber("470813-8895");
        externalCustomer.setShoppingLists(Collections.singletonList(new ShoppingList("lipstick", "blusher")));
        externalCustomer.setPreferredStore(null);
        return externalCustomer;
    }

    static Customer personCustomerMatchingByExternalId(ExternalCustomer externalCustomer) {
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.PERSON);
        customer.setExternalId(externalCustomer.getExternalId());
        customer.setInternalId(UUID.randomUUID().toString());
        return customer;
    }

    static Customer companyCustomerMatchingByExternalIdAndCompanyNumber(ExternalCustomer externalCustomer) {
        Customer customer = new Customer();
        customer.setCompanyNumber(externalCustomer.getCompanyNumber());
        customer.setCustomerType(CustomerType.COMPANY);
        customer.setExternalId(externalCustomer.getExternalId());
        customer.setInternalId(UUID.randomUUID().toString());
        return customer;
    }

    static Customer companyCustomerMatchingByExternalIdAndDifferentCompanyNumber(ExternalCustomer externalCustomer) {
        Customer customer = new Customer();
        customer.setCompanyNumber(UUID.randomUUID().toString());
        customer.setCustomerType(CustomerType.COMPANY);
        customer.setExternalId(externalCustomer.getExternalId());
        customer.setInternalId(UUID.randomUUID().toString());
        return customer;
    }

    static Customer companyCustomerMatchingByCompanyNumberButDifferentExternalId(ExternalCustomer externalCustomer) {
        Customer customer = new Customer();
        customer.setCompanyNumber(externalCustomer.getCompanyNumber());
        customer.setCustomerType(CustomerType.COMPANY);
        customer.setExternalId(UUID.randomUUID().toString());
        customer.setInternalId(UUID.randomUUID().toString());
        return customer;
    }

    static Customer companyCustomerMatchingByExternalIdAndMasterExternalId(ExternalCustomer externalCustomer) {
        Customer customer = new Customer();
        customer.setCompanyNumber(externalCustomer.getCompanyNumber());
        customer.setCustomerType(CustomerType.COMPANY);
        customer.setExternalId(externalCustomer.getExternalId());
        customer.setMasterExternalId(externalCustomer.getExternalId());
        customer.setInternalId(UUID.randomUUID().toString());
        return customer;
    }

    static Customer companyCustomerMatchingByCompanyNumberAndNullExternalId(ExternalCustomer externalCustomer) {
        Customer customer = new Customer();
        customer.setCompanyNumber(externalCustomer.getCompanyNumber());
        customer.setCustomerType(CustomerType.COMPANY);
        customer.setExternalId(null);
        customer.setInternalId(UUID.randomUUID().toString());
        return customer;
    }
}
