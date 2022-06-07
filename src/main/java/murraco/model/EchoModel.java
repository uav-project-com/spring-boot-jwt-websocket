package murraco.model;

/**
 * Created by Naik on 23.02.17.
 */
public class EchoModel {
    public EchoModel(String echo) {
        this.echo = echo;
    }

    public String getEcho() {
        return echo;
    }

    public void setEcho(String echo) {
        this.echo = echo;
    }

    private String echo;
}
