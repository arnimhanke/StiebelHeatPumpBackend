package de.hank.arnim.heizung;

/**
 * Created by arnim on 12/24/17.
 */
public abstract class AbstractInfo {

    public String url;

    public AbstractInfo(String url) {
        this.url = url;
    }

    public abstract void getInformations(String content, long time);

}
