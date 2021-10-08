package dtos;

import entities.Address;

import java.util.Objects;

public class AddressDTO {
    private long id;
    private String street;
    private String additionalInfo;
    private CityInfoDTO cityInfo;

    public AddressDTO(Address a) {
        this.id = a.getId();
        this.street = a.getStreet();
        this.additionalInfo = a.getAdditionalInfo();
        this.cityInfo = new CityInfoDTO(a.getCityInfo());
    }

    public AddressDTO(String s, String add) {
        this.street = s;
        this.additionalInfo = add;
    }

    public AddressDTO(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public CityInfoDTO getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfoDTO cityInfo) {
        this.cityInfo = cityInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDTO that = (AddressDTO) o;
        return id == that.id && street.equals(that.street) && additionalInfo.equals(that.additionalInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, additionalInfo);
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", cityInfo=" + cityInfo +
                '}';
    }
}
