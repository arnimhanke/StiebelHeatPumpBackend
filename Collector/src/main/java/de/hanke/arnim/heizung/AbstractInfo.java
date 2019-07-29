package de.hanke.arnim.heizung;

import de.hanke.arnim.common.Utils;

/**
 * Created by arnim on 12/24/17.
 */
public abstract class AbstractInfo {

    public String url;
    public Utils utils = new Utils();

    public AbstractInfo() {}

    public void setUrl(String url) {
        this.url = url;
    }

    public abstract void getInformations(String content, long time);

}
