package codingdojo;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class CustomerAssert extends AbstractAssert<CustomerAssert, Customer> {
    public CustomerAssert(Customer customer) {
        super(customer, CustomerAssert.class);
    }

    public static CustomerAssert assertThat(Customer actual) {
        return new CustomerAssert(actual);
    }

    public CustomerAssert isCompany() {
        isNotNull();
        if (actual.getCustomerType() != CustomerType.COMPANY) {
            failWithMessage("Expected customer to be a company, but was %s", actual.getCustomerType());
        }
        return this;
    }

    public CustomerAssert isPerson() {
        isNotNull();
        if (actual.getCustomerType() != CustomerType.PERSON) {
            failWithMessage("Expected customer to be a person, but was %s", actual.getCustomerType());
        }
        return this;
    }

    public CustomerAssert hasEmptyMasterExternalId() {
        isNotNull();
        if (actual.getMasterExternalId() != null) {
            failWithMessage("Expected master external id to be empty, but was %s", actual.getMasterExternalId());
        }
        return this;
    }

    public CustomerAssert wasUpdatedFrom(ExternalCustomer externalCustomer) {
        isNotNull();

        Assertions.assertThat(actual.getName()).isEqualTo(externalCustomer.getName());
        Assertions.assertThat(actual.getExternalId()).isEqualTo(externalCustomer.getExternalId());
        Assertions.assertThat(actual.getCompanyNumber()).isEqualTo(externalCustomer.getCompanyNumber());
        Assertions.assertThat(actual.getAddress()).isEqualTo(externalCustomer.getPostalAddress());
        Assertions.assertThat(actual.getShoppingLists()).isEqualTo(externalCustomer.getShoppingLists());
        Assertions.assertThat(actual.getPreferredStore()).isEqualTo(externalCustomer.getPreferredStore());

        return this;
    }

    public CustomerAssert hasBonusPoints(ExternalCustomer externalCustomer) {
        isNotNull();

        Assertions.assertThat(actual.getBonusPointsBalance()).isEqualTo(externalCustomer.getBonusPointsBalance());

        return this;
    }

    public CustomerAssert hasNoBonusPoints() {
        isNotNull();

        Assertions.assertThat(actual.getBonusPointsBalance()).isNull();

        return this;
    }

}
