package ttr.rute;

import java.io.Serializable;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.spelar.PlayerAndNetworkWTF;

public interface Route extends Serializable {
    Destinasjon getStart();
    Destinasjon getEnd();
    int getLength();
    int getValue();
    Farge getColour();
    boolean isTunnel();
    int getNumberOfRequiredJokers();
    void setBuiltBy(PlayerAndNetworkWTF player);
}
