package de.hank.arnim.dtos;

public class GriddleTableValueDto {

    public String einheitAsString;
    public String type;
    public String data;

    public GriddleTableValueDto(String einheitAsString, String type, String data) {
        this.einheitAsString = einheitAsString;
        this.type = type;
        this.data = data;
    }

}
