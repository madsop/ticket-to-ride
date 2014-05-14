package ttr.bygg;

import ttr.spelar.ISpelar;

public class ByggjandeInfo {
    public final ISpelar byggjandeSpelar;
    public final int jokrar;

    public ByggjandeInfo(ISpelar byggjandeSpelar, int jokrar) {
        this.byggjandeSpelar = byggjandeSpelar;
        this.jokrar = jokrar;
    }
}
