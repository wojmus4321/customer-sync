package codingdojo;

import java.util.Collection;

public class CustomerDataAccess {

    private final CustomerDataLayer customerDataLayer;

    public CustomerDataAccess(CustomerDataLayer customerDataLayer) {
        this.customerDataLayer = customerDataLayer;
    }

    public CustomerMatches loadPerson(ExternalCustomer externalCustomer) {
        final String externalId = externalCustomer.getExternalId();
        Customer matchByExternalId = this.customerDataLayer.findByExternalId(externalId);

        CustomerMatches matches = new CustomerMatches();
        if (matchByExternalId != null) {
            matches.matchesByExternalId(matchByExternalId);
        }

        return matches;
    }

    public CustomerMatches loadCompany(ExternalCustomer externalCustomer) {
        final String externalId = externalCustomer.getExternalId();
        final String companyNumber = externalCustomer.getCompanyNumber();
        Customer matchByExternalId = this.customerDataLayer.findByExternalId(externalId);

        CustomerMatches matches = new CustomerMatches();
        if (matchByExternalId != null) {
            matches.matchesByExternalId(matchByExternalId);
            Customer matchByMasterId = this.customerDataLayer.findByMasterExternalId(externalId);
            if (matchByMasterId != null) {
                matches.addDuplicate(matchByMasterId);
            }
        } else {
            Customer matchByCompanyNumber = this.customerDataLayer.findByCompanyNumber(companyNumber);
            if (matchByCompanyNumber != null) {
                matches.matchesByCompanyNumber(matchByCompanyNumber);
            }
        }

        return matches;
    }

    public Customer save(Customer customer) {
        return customer.getInternalId() != null ?
                customerDataLayer.updateCustomerRecord(customer) :
                customerDataLayer.createCustomerRecord(customer);
    }

    public void save(Collection<Customer> customers) {
        for (Customer customer : customers) {
            if (customer.getInternalId() != null) {
                customerDataLayer.updateCustomerRecord(customer);
            } else {
                customerDataLayer.createCustomerRecord(customer);
            }
        }
    }
}
