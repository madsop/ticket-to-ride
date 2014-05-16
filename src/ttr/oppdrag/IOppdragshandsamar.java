package ttr.oppdrag;

import java.util.ArrayList;

public interface IOppdragshandsamar {
    ArrayList<Mission> getRemainingMissions();

    int getNumberOfRemainingMissions();

    Mission getMissionAndRemoveItFromDeck();
}
