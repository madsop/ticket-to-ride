package ttr.listeners;

import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.oppdrag.listeners.TrekkOppdragHandler;
import ttr.oppdrag.listeners.ShowMyMissionsHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class DelegationListener implements ActionListener{
    private final IGUI gui;
    private final IHovud hovud;
    private final JButton visBygde,visMineKort,visMineOppdrag,trekkOppdrag,bygg;
    private final JFrame frame;

    public DelegationListener(IGUI gui, IHovud hovud, JButton visBygde,
                            JButton visMineKort, JButton visMineOppdrag, JButton trekkOppdrag, JButton bygg,
                            JFrame frame) {
        this.gui = gui;
        this.hovud = hovud;
        this.visBygde = visBygde;
        this.visMineKort = visMineKort;
        this.visMineOppdrag = visMineOppdrag;
        this.trekkOppdrag = trekkOppdrag;
        this.bygg = bygg;
        this.frame = frame;
    }


    public void actionPerformed(ActionEvent arg0) {
        try {
            if (hovud.isNett() && (!hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())) ) {
                if (!(arg0.getSource() == visBygde || arg0.getSource() == visMineKort || arg0.getSource() == visMineOppdrag)) {
                    JOptionPane.showMessageDialog((Component) gui, "Det er ikkje din tur!");
                    return;
                }
            }

            if (arg0.getSource() == trekkOppdrag) {
                new TrekkOppdragHandler(hovud,gui);
            }
            else if (arg0.getSource() == bygg) {
                new ByggHandler(hovud,frame);
            }
            else if (arg0.getSource() == visMineKort) {
                new ShowMyCardsHandler(hovud,gui);
            }
            else if (arg0.getSource() == visMineOppdrag) {
                new ShowMyMissionsHandler(hovud,gui);
            }
            else if (arg0.getSource() == visBygde) {
                new visBygdeHandler(hovud,gui,frame);
            }
        }
        catch (RemoteException e2) {
            e2.printStackTrace();
        }
    }
}
