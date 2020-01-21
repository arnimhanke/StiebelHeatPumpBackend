package de.hank.arnim.utils;

public enum Raster {


    PT15_SEC("PT15_SEC"),
    PT1_DAY("PT1_DAY");

    private String name;

    Raster(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
