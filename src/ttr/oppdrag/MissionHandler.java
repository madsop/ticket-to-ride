package ttr.oppdrag;

import java.util.ArrayList;

public interface MissionHandler {
    ArrayList<Mission> getRemainingMissions();

    int getNumberOfRemainingMissions();

    Mission getMissionAndRemoveItFromDeck();
}