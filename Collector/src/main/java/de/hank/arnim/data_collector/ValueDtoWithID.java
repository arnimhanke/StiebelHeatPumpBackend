package de.hank.arnim.data_collector;

import de.hank.arnim.common.ValueDto;

/**
 * Created by arnim on 2/13/18.
 */
public class ValueDtoWithID {

    private String id;
    private ValueDto valueDto;

    public ValueDtoWithID(ValueDto valueDto, String id) {
        this.id = id;
        this.valueDto = valueDto;
    }

    public ValueDtoWithID() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ValueDto getValueDto() {
        return valueDto;
    }

    public void setValueDto(ValueDto valueDto) {
        this.valueDto = valueDto;
    }
}
