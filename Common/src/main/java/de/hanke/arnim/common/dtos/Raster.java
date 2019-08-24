package de.hanke.arnim.common.dtos;

public enum Raster {


    PT15M("PT15M"),

    PT1S("PT1S"),

    PT15S ("PT15S"),

    PT1D("PT1D"),

    PT1H("PT1H");

    private String value;

    Raster(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static Raster fromValue(String text) {
        for (Raster b : Raster.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

}
