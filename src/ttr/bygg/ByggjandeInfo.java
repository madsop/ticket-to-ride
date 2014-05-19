package ttr.bygg;

import ttr.spelar.ISpelar;

public class ByggjandeInfo {
    public final ISpelar byggjandeSpelar;
    public final int jokrar;
    public final int position;

    public ByggjandeInfo(ISpelar byggjandeSpelar, int jokrar, int position) {
        this.byggjandeSpelar = byggjandeSpelar;
        this.jokrar = jokrar;
        this.position = position;
    }
}
