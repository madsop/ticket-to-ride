package ttr.bygg;

import ttr.data.Colour;
import ttr.spelar.PlayerAndNetworkWTF;

public class ByggjandeInfo {
    public final PlayerAndNetworkWTF byggjandeSpelar;
    public final int jokrar;
	public final Colour colour;

    public ByggjandeInfo(PlayerAndNetworkWTF byggjandeSpelar, int jokrar, Colour colour) {
        this.byggjandeSpelar = byggjandeSpelar;
        this.jokrar = jokrar;
        this.colour = colour;
    }
}
