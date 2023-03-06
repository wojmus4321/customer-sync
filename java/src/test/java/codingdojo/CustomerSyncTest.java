package codingdojo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static codingdojo.CustomerAssert.assertThat;


public class CustomerSyncTest {

    private CustomerDataLayer db;
    private CustomerSync sut;

    @BeforeEach
    void setUp() {
        db = new InMemoryCustomerDataLayer();
        sut = new CustomerSync(db);
    }

    @Test
    @DisplayName("Case1: " +
            "given external company customer and existing company customer matching by externalId " +
            "when sync is performed " +
            "then existing company customer is updated with external company customer data correctly")
    public void syncCompanyByExternalId() {
        // given

        String externalId = "12345";

        ExternalCustomer externalCustomer = createExternalCompany(externalId);
        Customer customer = createCustomerWithSameCompanyAs(externalCustomer);

        db.createCustomerRecord(customer);

        // when
        boolean created = sut.syncWithDataLayer(externalCustomer);

        // then
        Assertions.assertThat(created).isFalse();
        Customer updatedCustomer = db.findByExternalId(externalCustomer.getExternalId());

        assertThat(updatedCustomer)
                .isCompany()
                .hasEmptyMasterExternalId()
                .wasUpdatedFrom(externalCustomer);
    }


    private ExternalCustomer createExternalCompany(String externalId) {
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(externalId);
        externalCustomer.setName("Acme Inc.");
        externalCustomer.setAddress(new Address("123 main st", "Helsingborg", "SE-123 45"));
        externalCustomer.setCompanyNumber("470813-8895");
        externalCustomer.setShoppingLists(Collections.singletonList(new ShoppingList("lipstick", "blusher")));
        return externalCustomer;
    }

    private Customer createCustomerWithSameCompanyAs(ExternalCustomer externalCustomer) {
        Customer customer = new Customer();
        customer.setCompanyNumber(externalCustomer.getCompanyNumber());
        customer.setCustomerType(CustomerType.COMPANY);
        customer.setExternalId(externalCustomer.getExternalId());
        customer.setInternalId("45435");
        return customer;
    }

}
