package dtos;

import entities.CityInfo;

import java.util.Objects;

public class CityInfoDTO {
    private String zipCode;
    private String city;

    public CityInfoDTO(CityInfo ci) {
        this.zipCode = ci.getZipCode();
        this.city = ci.getCity();
    }

    public CityInfoDTO(String zip, String city) {
        this.zipCode = zip;
        this.city = city;
    }

    public CityInfoDTO() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityInfoDTO that = (CityInfoDTO) o;
        return Objects.equals(zipCode, that.zipCode) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipCode, city);
    }
}