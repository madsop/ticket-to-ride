package ttr.oppdrag;

import java.util.ArrayList;

public interface IOppdragshandsamar {
    ArrayList<IOppdrag> getRemainingMissions();

    int getNumberOfRemainingMissions();

    IOppdrag getMissionAndRemoveItFromDeck();
}
