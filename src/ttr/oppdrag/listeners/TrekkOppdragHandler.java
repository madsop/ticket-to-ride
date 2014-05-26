package ttr.oppdrag.listeners;

import java.rmi.RemoteException;

import ttr.data.Colour;
import ttr.gui.GUI;
import ttr.kjerna.Core;
import ttr.oppdrag.MissionHandler;

public class TrekkOppdragHandler {
    public TrekkOppdragHandler(Core core, GUI gui, MissionHandler missionHandler) throws RemoteException {
            core.sendMessageAboutCard(false, false, Colour.blå);
            missionHandler.trekkOppdrag(gui, core.findPlayerInAction(), false);
            core.nesteSpelar();
    }
}