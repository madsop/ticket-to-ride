package ttr.rute;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.spelar.PlayerAndNetworkWTF;

public interface Route {
    int getRouteId();
    int getValue();

    Destinasjon getStart();
    Destinasjon getEnd();

    int getLength();

    Farge getColour();

    boolean isTunnel();

    int getNumberOfRequiredJokers();

    void setBuiltBy(PlayerAndNetworkWTF player);
}
