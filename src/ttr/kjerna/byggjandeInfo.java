package ttr.kjerna;

import ttr.spelar.ISpelar;

class byggjandeInfo {
    final ISpelar byggjandeSpelar;
    final int jokrar;
    byggjandeInfo(ISpelar byggjandeSpelar, int jokrar) {
        this.byggjandeSpelar = byggjandeSpelar;
        this.jokrar = jokrar;
    }
}
