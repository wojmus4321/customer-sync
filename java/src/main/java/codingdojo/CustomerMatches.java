package codingdojo;

import java.util.ArrayList;
import java.util.Collection;

public class CustomerMatches {

    public void matchesByExternalId(Customer matchByExternalId) {
        this.matchTerm = MatchTerm.EXTERNAL_ID;
        this.customer = matchByExternalId;
    }

    public void matchesByCompanyNumber(Customer matchByCompanyNumber) {
        this.matchTerm = MatchTerm.COMPANY_NUMBER;
        this.customer = matchByCompanyNumber;
    }

    public void matchesNone() {
        this.matchTerm = null;
        this.customer = null;
    }

    public enum MatchTerm {
        EXTERNAL_ID, COMPANY_NUMBER
    }
    private final Collection<Customer> duplicates = new ArrayList<>();
    private MatchTerm matchTerm;
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void addDuplicate(Customer duplicate) {
        duplicates.add(duplicate);
    }

    public Collection<Customer> getDuplicates() {
        return duplicates;
    }

    public boolean isMatchingByExternalId() {
        return matchTerm == MatchTerm.EXTERNAL_ID;
    }

    public boolean isMatchingByCompanyNumber() {
        return matchTerm == MatchTerm.COMPANY_NUMBER;
    }
}
