package codingdojo;

import java.util.ArrayList;
import java.util.Collection;

public class CustomerMatches {

    public enum MatchTerm {
        EXTERNAL_ID, COMPANY_NUMBER
    }
    private Collection<Customer> duplicates = new ArrayList<>();
    private MatchTerm matchTerm;
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public boolean hasDuplicates() {
        return !duplicates.isEmpty();
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

    public void setMatchTerm(MatchTerm matchTerm) {
        this.matchTerm = matchTerm;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
