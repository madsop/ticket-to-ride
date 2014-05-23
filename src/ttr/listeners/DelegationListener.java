package ttr.listeners;

import ttr.gui.IGUI;
import ttr.kjerna.Core;
import ttr.oppdrag.listeners.TrekkOppdragHandler;
import ttr.oppdrag.listeners.ShowMyMissionsHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class DelegationListener implements ActionListener{
    private final IGUI gui;
    private final Core hovud;
    private final JButton visBygde,visMineKort,visMineOppdrag,trekkOppdrag,bygg;
    private final JFrame frame;
	private boolean nett;

    public DelegationListener(IGUI gui, Core hovud, JButton visBygde,
                            JButton visMineKort, JButton visMineOppdrag, JButton trekkOppdrag, JButton bygg,
                            JFrame frame, boolean nett) {
        this.gui = gui;
        this.hovud = hovud;
        this.visBygde = visBygde;
        this.visMineKort = visMineKort;
        this.visMineOppdrag = visMineOppdrag;
        this.trekkOppdrag = trekkOppdrag;
        this.bygg = bygg;
        this.frame = frame;
        this.nett = nett;
    }


    //TODO sørg for at du ikkje får gjort anna enn å trekke kort etter du har trekt det første kortet
    public void actionPerformed(ActionEvent arg0) {
        try {
            if (nett && (!hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())) ) {
                if (!(arg0.getSource() == visBygde || arg0.getSource() == visMineKort || arg0.getSource() == visMineOppdrag)) {
                    JOptionPane.showMessageDialog((Component) gui, "Det er ikkje din tur!");
                    return;
                }
            }

            if (arg0.getSource() == trekkOppdrag) {
                new TrekkOppdragHandler(hovud,gui);
            }
            else if (arg0.getSource() == bygg) {
                new BuildRouteHandler(hovud,frame);
            }
            else if (arg0.getSource() == visMineKort) {
                new ShowMyCardsHandler(gui, hovud.findPlayerInAction());
            }
            else if (arg0.getSource() == visMineOppdrag) {
                new ShowMyMissionsHandler(gui, hovud.findPlayerInAction());
            }
            else if (arg0.getSource() == visBygde) {
                new ShowBuiltRoutesHandler(hovud,gui,frame);
            }
        }
        catch (RemoteException e2) {
            e2.printStackTrace();
        }
    }
}
