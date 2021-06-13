package de.hanke.arnim;

public class Properties {

    public static final String ADDRESS_INFLUXDB = System.getenv("address.influxdb");
    public static final String USER_INFLUXDB = System.getenv("user.influxdb");
    public static final String PASSWORD_INFLUXDB = System.getenv("password.influxdb");
    public static final String DATABASE_RAW_DATA_INFLUXDB = System.getenv("database.raw.influxdb");
}
