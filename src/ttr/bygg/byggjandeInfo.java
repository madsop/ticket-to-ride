package ttr.bygg;

import ttr.spelar.ISpelar;

public class byggjandeInfo {
    public final ISpelar byggjandeSpelar;
    public final int jokrar;

    public byggjandeInfo(ISpelar byggjandeSpelar, int jokrar) {
        this.byggjandeSpelar = byggjandeSpelar;
        this.jokrar = jokrar;
    }
}
