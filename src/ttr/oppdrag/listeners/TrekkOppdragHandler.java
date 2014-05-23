package ttr.oppdrag.listeners;

import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.kjerna.Core;
import ttr.oppdrag.MissionHandlerImpl;

import java.rmi.RemoteException;

public class TrekkOppdragHandler {
    public TrekkOppdragHandler(Core hovud, IGUI gui) throws RemoteException {
            hovud.sendMessageAboutCard(false, false, Konstantar.FARGAR[0]);
            MissionHandlerImpl.trekkOppdrag(gui, hovud.findPlayerInAction(), false);
            hovud.nesteSpelar();
    }
}
