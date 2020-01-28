package de.hanke.arnim.common.dtos;

import de.hanke.arnim.common.ValueDto;

import java.util.List;
import java.util.Map;

/**
 * Created by arnim on 1/5/18.
 */
public class MonthViewDataDto {

    private Map<String, List<ValueDto>> values;
    private Map<String, String> displayedNames;

    public MonthViewDataDto() {
    }

    public MonthViewDataDto(Map<String, List<ValueDto>> values, Map<String, String> displayedNames) {
        this.values = values;
        this.displayedNames = displayedNames;
    }

    public Map<String, List<ValueDto>> getValues() {
        return values;
    }

    public void setValues(Map<String, List<ValueDto>> values) {
        this.values = values;
    }

    public Map<String, String> getDisplayedNames() {
        return displayedNames;
    }

    public void setDisplayedNames(Map<String, String> displayedNames) {
        this.displayedNames = displayedNames;
    }
}
