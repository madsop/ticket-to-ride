package ttr.oppdrag.listeners;

import ttr.data.Konstantar;
import ttr.gui.GUI;
import ttr.kjerna.Core;
import ttr.oppdrag.MissionHandler;

import java.rmi.RemoteException;

public class TrekkOppdragHandler {
    public TrekkOppdragHandler(Core hovud, GUI gui) throws RemoteException {
            hovud.sendMessageAboutCard(false, false, Konstantar.FARGAR[0]);
            MissionHandler.trekkOppdrag(gui, hovud.findPlayerInAction(), false);
            hovud.nesteSpelar();
    }
}
