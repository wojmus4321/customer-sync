package codingdojo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static codingdojo.CustomerAssert.assertThat;
import static codingdojo.TestData.*;


@DisplayName("Customer sync tests")
public class CustomerSyncTest {

    private CustomerDataLayer db;
    private CustomerSync sut;

    @BeforeEach
    void setUp() {
        db = new InMemoryCustomerDataLayer();
        sut = new CustomerSync(db);
    }

    @Nested
    @DisplayName("Person Customer cases")
    class PersonCustomer {
        @Test
        @DisplayName("" +
                "given external person customer and no existing matching customer, " +
                "when sync is performed, " +
                "then 1 new customer record is created")
        public void case1() {
            // given
            ExternalCustomer externalCustomer = anExternalPerson();
            long beforeCount = db.countCustomers();

            // when
            boolean created = sut.syncWithDataLayer(externalCustomer);

            // then
            Assertions.assertThat(db.countCustomers() - beforeCount).isEqualTo(1L);
            Assertions.assertThat(created).isTrue();
            Customer updatedCustomer = db.findByExternalId(externalCustomer.getExternalId());

            assertThat(updatedCustomer)
                    .isPerson()
                    .wasUpdatedFrom(externalCustomer)
                    .hasBonusPoints(externalCustomer);
        }

        @Test
        @DisplayName("" +
                "given external person customer and existing matching person customer by externalId, " +
                "when sync is performed, " +
                "then 1 customer record is updated")
        public void case2() {
            // given
            ExternalCustomer externalCustomer = anExternalPerson();
            Customer matchingCustomer = personCustomerMatchingByExternalId(externalCustomer);
            db.createCustomerRecord(matchingCustomer);
            long beforeCount = db.countCustomers();

            // when
            boolean created = sut.syncWithDataLayer(externalCustomer);

            // then
            Assertions.assertThat(db.countCustomers() - beforeCount).isEqualTo(0L);
            Assertions.assertThat(created).isFalse();
            Customer updatedCustomer = db.findByExternalId(externalCustomer.getExternalId());

            assertThat(updatedCustomer)
                    .isPerson()
                    .wasUpdatedFrom(externalCustomer)
                    .hasBonusPoints(externalCustomer);
        }

        @Test
        @DisplayName("" +
                "given external person customer and existing matching company customer by externalId, " +
                "when sync is performed, " +
                "then ConflictException is thrown")
        public void case3() {
            // given
            ExternalCustomer externalCustomer = anExternalPerson();
            Customer matchingCustomer = companyCustomerMatchingByExternalIdAndCompanyNumber(externalCustomer);
            db.createCustomerRecord(matchingCustomer);

            // then
            org.junit.jupiter.api.Assertions.assertThrows(ConflictException.class, () -> sut.syncWithDataLayer(externalCustomer));

        }
    }

    @Nested
    @DisplayName("Company Customer cases")
    class CompanyCustomer {

        @Test
        @DisplayName("" +
                "given external company customer and no existing matching customer, " +
                "when sync is performed, " +
                "then 1 new customer record is created")
        public void case1() {
            // given
            ExternalCustomer externalCustomer = anExternalCompany();
            long beforeCount = db.countCustomers();

            // when
            boolean created = sut.syncWithDataLayer(externalCustomer);

            // then
            Assertions.assertThat(db.countCustomers() - beforeCount).isEqualTo(1L);
            Assertions.assertThat(created).isTrue();
            Customer updatedCustomer = db.findByExternalId(externalCustomer.getExternalId());

            assertThat(updatedCustomer)
                    .isCompany()
                    .wasUpdatedFrom(externalCustomer)
                    .hasNoBonusPoints();
        }

        @Test
        @DisplayName("" +
                "given external company customer and existing company customer matching by externalId and companyNumber, " +
                "when sync is performed, " +
                "then 1 customer record is updated")
        public void case2() {
            // given
            ExternalCustomer externalCustomer = anExternalCompany();
            Customer matchingCustomer = companyCustomerMatchingByExternalIdAndCompanyNumber(externalCustomer);
            db.createCustomerRecord(matchingCustomer);
            long beforeCount = db.countCustomers();

            // when
            boolean created = sut.syncWithDataLayer(externalCustomer);

            // then
            Assertions.assertThat(db.countCustomers() - beforeCount).isEqualTo(0L);
            Assertions.assertThat(created).isFalse();
            Customer updatedCustomer = db.findByExternalId(externalCustomer.getExternalId());

            assertThat(updatedCustomer)
                    .isCompany()
                    .hasEmptyMasterExternalId()
                    .wasUpdatedFrom(externalCustomer)
                    .hasNoBonusPoints();
        }

        @Test
        @DisplayName("" +
                "given external company customer and existing matching company customer by externalId, but not companyNumber, " +
                "when sync is performed, " +
                "then 1 customer record is created and 1 customer duplicate record is updated")
        public void case3() {
            // given
            ExternalCustomer externalCustomer = anExternalCompany();
            Customer matchingCustomer = companyCustomerMatchingByExternalIdAndDifferentCompanyNumber(externalCustomer);
            db.createCustomerRecord(matchingCustomer);
            long beforeCount = db.countCustomers();

            // when
            boolean created = sut.syncWithDataLayer(externalCustomer);
            long afterCount = db.countCustomers();

            // then
            Assertions.assertThat(afterCount - beforeCount).isEqualTo(1L);
            Assertions.assertThat(created).isTrue();
            Customer updatedCustomer = db.findByCompanyNumber(externalCustomer.getCompanyNumber());

            assertThat(updatedCustomer)
                    .isCompany()
                    .wasUpdatedFrom(externalCustomer)
                    .hasNoBonusPoints();

            Customer createdCustomer = db.findByCompanyNumber(matchingCustomer.getCompanyNumber());

            assertThat(createdCustomer)
                    .isCompany()
                    .hasNoBonusPoints();
        }

        @Test
        @DisplayName("" +
                "given external company customer and existing matching company customer by companyNumber, but not externalId, " +
                "when sync is performed, " +
                "then ConflictException is thrown")
        public void case4() {
            // given
            ExternalCustomer externalCustomer = anExternalCompany();
            Customer matchingCustomer = companyCustomerMatchingByCompanyNumberButDifferentExternalId(externalCustomer);
            db.createCustomerRecord(matchingCustomer);

            // then
            org.junit.jupiter.api.Assertions.assertThrows(ConflictException.class, () -> sut.syncWithDataLayer(externalCustomer));
        }

        @Test
        @DisplayName("" +
                "given external company customer and existing matching person customer by externalId, " +
                "when sync is performed, " +
                "then ConflictException is thrown")
        public void case5() {
            // given
            ExternalCustomer externalCustomer = anExternalCompany();
            Customer matchingCustomer = personCustomerMatchingByExternalId(externalCustomer);
            db.createCustomerRecord(matchingCustomer);

            // then
            org.junit.jupiter.api.Assertions.assertThrows(ConflictException.class, () -> sut.syncWithDataLayer(externalCustomer));

        }

        @Test
        @DisplayName("" +
                "given external company customer and existing matching company customer by externalId and masterExternalId, " +
                "when sync is performed, " +
                "then 1 customer record is updated")
        public void case6() {
            // given
            ExternalCustomer externalCustomer = anExternalCompany();
            Customer matchingCustomer = companyCustomerMatchingByExternalIdAndMasterExternalId(externalCustomer);
            db.createCustomerRecord(matchingCustomer);
            long beforeCount = db.countCustomers();

            // when
            boolean created = sut.syncWithDataLayer(externalCustomer);

            // then
            Assertions.assertThat(db.countCustomers() - beforeCount).isEqualTo(0L);
            Assertions.assertThat(created).isFalse();
            Customer updatedCustomer = db.findByExternalId(externalCustomer.getExternalId());

            assertThat(updatedCustomer)
                    .isCompany()
                    .wasUpdatedFrom(externalCustomer)
                    .hasNoBonusPoints();

        }

        @Test
        @DisplayName("" +
                "given external company customer and existing matching company customer by companyNumber with null externalId, " +
                "when sync is performed, " +
                "then 1 customer record is created and 1 duplicate record is updated")
        public void case7() {
            // given
            ExternalCustomer externalCustomer = anExternalCompany();
            Customer matchingCustomer = companyCustomerMatchingByCompanyNumberAndNullExternalId(externalCustomer);
            db.createCustomerRecord(matchingCustomer);
            long beforeCount = db.countCustomers();

            // when
            boolean created = sut.syncWithDataLayer(externalCustomer);

            // then
            Assertions.assertThat(db.countCustomers() - beforeCount).isEqualTo(1L);
            Assertions.assertThat(created).isFalse();
            Customer updatedCustomer = db.findByCompanyNumber(externalCustomer.getCompanyNumber());

            assertThat(updatedCustomer)
                    .isCompany()
                    .wasUpdatedFrom(externalCustomer)
                    .hasNoBonusPoints();
        }
    }
}
