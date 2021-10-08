package dtos;

import entities.Hobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HobbyDTO {
    private long id;
    private String name;
    private String wikiLink;
    private String category;
    private String type;

    public HobbyDTO(Hobby h) {
        this.id = h.getId();
        this.name = h.getName();
        this.wikiLink = h.getWikiLink();
        this.category = h.getCategory();
        this.type = h.getType();
    }

    public HobbyDTO(String n, String wiki) {
        this.name = n;
        this.wikiLink = wiki;
    }

    public HobbyDTO(){
    }

    public static List<HobbyDTO> getHobbiesFromList(List<Hobby> h) {
        List<HobbyDTO> hDto = new ArrayList<>();
        for (Hobby hobby: h) {
            hDto.add(new HobbyDTO(hobby));
        }
        return hDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWikiLink() {
        return wikiLink;
    }

    public void setWikiLink(String wikiLink) {
        this.wikiLink = wikiLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HobbyDTO hobbyDTO = (HobbyDTO) o;
        return id == hobbyDTO.id && name.equals(hobbyDTO.name) && wikiLink.equals(hobbyDTO.wikiLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, wikiLink);
    }
}
