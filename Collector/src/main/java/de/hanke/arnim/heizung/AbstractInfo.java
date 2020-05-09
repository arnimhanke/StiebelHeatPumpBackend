package de.hanke.arnim.heizung;

import de.hanke.arnim.common.ElasticSearchUtils;

/**
 * Created by arnim on 12/24/17.
 */
public abstract class AbstractInfo {

    public String url;
    public ElasticSearchUtils elasticSearchUtils = new ElasticSearchUtils();

    public AbstractInfo() {}

    public void setUrl(String url) {
        this.url = url;
    }

    public abstract void getInformations(String content, long time);

    public String getInformation(String content, String tabelName, String key, String esType) {
        try {
            if (content.split(tabelName + "</th>").length == 2) {
                String table = content.split(tabelName + "</th>")[1];
                if (table.split(".*" + key + "</td>" + ".*").length > 1) {
                    String[] split = table.split(">" + key + "</td>");
                    return split[1].split("</td>")[0].split("\">")[1];
                } else {
                    // Type nicht in der Website vorhanden, Type scheint ausgeschaltet zu sein => muss trotzdem registriert werden, ist ja eine Änderung!
                    return "";
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println(key + " - " + esType);
            return "";
        }
        return "";
    }

}
