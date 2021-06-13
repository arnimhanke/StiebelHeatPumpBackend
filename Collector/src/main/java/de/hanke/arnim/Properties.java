package de.hanke.arnim;

import de.hanke.arnim.common.ElasticSearchUtils;

public class Properties {

    public static final String ADDRESS_ISG = System.getenv("address.isg");
//    public static final String ADDRESS_ELASTICSEARCH = System.getenv("address.elasticsearch");
//    public static final String ADDRESS_ELASTICSEARCH_LOCALHOST = System.getenv("address.elasticsearch.localhost");
//    public static final int PORT_ELASTICSEARCH;

//    static {
//        String getenv = System.getenv("port.elasticsearch");
//        PORT_ELASTICSEARCH = Integer.parseInt(getenv != null ? getenv: "9200");
//    }

    public static final String PROTOCOL_ELASTICSEARCH = System.getenv("protocol.elasticsearch");

    public static final String ADDRESS_INFLUXDB = System.getenv("address.influxdb");
    public static final String USER_INFLUXDB = System.getenv("user.influxdb");
    public static final String PASSWORD_INFLUXDB = System.getenv("password.influxdb");
    public static final String DATABASE_RAW_DATA_INFLUXDB = System.getenv("database.raw.influxdb");
}
