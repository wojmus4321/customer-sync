package codingdojo;

import java.util.List;

public class ExternalCustomer {
    private Address address;
    private String name;
    private String preferredStore;
    private List<ShoppingList> shoppingLists;
    private String externalId;
    private String companyNumber;
    private Integer bonusPointsBalance;

    public String getExternalId() {
        return externalId;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public boolean isCompany() {
        return companyNumber != null;
    }

    public Address getPostalAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreferredStore() {
        return preferredStore;
    }

    public void setPreferredStore(String preferredStore) {
        this.preferredStore = preferredStore;
    }

    public List<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public Integer getBonusPointsBalance() {
        return bonusPointsBalance;
    }

    public void setBonusPointsBalance(Integer bonusPointsBalance) {
        this.bonusPointsBalance = bonusPointsBalance;
    }


}
