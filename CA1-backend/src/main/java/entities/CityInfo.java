package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name = "CityInfo.deleteAllRows", query = "DELETE from CityInfo")
@Table (name = "cityinfo")
public class CityInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String zipCode;
    public CityInfo() {
    }


    private String city;

    @OneToMany
    List<Address> addresses;

    public CityInfo(String zip, String city) {
        this.zipCode = zip;
        this.city = city;
        addresses = new ArrayList<>();
    }

    public void removeAddress(Address address) {
        if (address != null) {
            addresses.remove(address);
            address.setCityInfo(null);
        }
    }

    public void addAddress(Address address) {
        if (address != null) {
            if (!addresses.contains(address)) {
                this.addresses.add(address);
                address.setCityInfo(this);
            } else {
                address.setCityInfo(this);
            }
        }
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "CityInfo{" +
                "zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", addresses=" + addresses +
                '}';
    }
}