package ttr.bygg;

import ttr.data.Colour;
import ttr.spelar.IPlayer;

public class ByggjandeInfo {
    public final IPlayer byggjandeSpelar;
    public final int jokrar;
	public final Colour colour;

    public ByggjandeInfo(IPlayer iPlayer, int jokrar, Colour colour) {
        this.byggjandeSpelar = iPlayer;
        this.jokrar = jokrar;
        this.colour = colour;
    }
}