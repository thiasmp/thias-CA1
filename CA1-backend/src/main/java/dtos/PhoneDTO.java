package dtos;

import entities.Hobby;
import entities.Phone;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhoneDTO {
    private long id;
    private String number;
    private String description;

    public PhoneDTO(Phone p) {
        this.id = p.getId();
        this.number = p.getNumber();
        this.description = p.getDescription();
    }

    public PhoneDTO(String n, String desc) {
        this.number = n;
        this.description = desc;
    }

    public PhoneDTO(){
    }

    public static List<PhoneDTO> getPhonesFromList(List<Phone> p) {
        List<PhoneDTO> pDto = new ArrayList<>();
        for (Phone phone: p) {
            pDto.add(new PhoneDTO(phone));
        }
        return pDto;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneDTO phoneDTO = (PhoneDTO) o;
        return id == phoneDTO.id && number.equals(phoneDTO.number) && description.equals(phoneDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, description);
    }

    @Override
    public String toString() {
        return "PhoneDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
