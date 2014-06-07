package ttr.listeners;

import ttr.kjerna.Core;
import ttr.oppdrag.ShowMyMissionsHandler;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class DelegationListener implements ActionListener {
    private final Core hovud;
    private final JButton visBygde,visMineKort,visMineOppdrag,trekkOppdrag,bygg;
    
	public DelegationListener(Core core, JButton visBygde, JButton visMineKort, JButton visMineOppdrag, JButton trekkOppdrag, JButton bygg) {
        this.hovud = core;
        this.visBygde = visBygde;
        this.visMineKort = visMineKort;
        this.visMineOppdrag = visMineOppdrag;
        this.trekkOppdrag = trekkOppdrag;
        this.bygg = bygg;
    }

    public void actionPerformed(ActionEvent arg0) {
        try {
            if (!hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())) {
                if (!(arg0.getSource() == visBygde || arg0.getSource() == visMineKort || arg0.getSource() == visMineOppdrag)) {
                    JOptionPane.showMessageDialog(null, "Det er ikkje din tur!");
                    return;
                }
            }
            if (hovud.findPlayerInAction().hasAlreadyDrawnOneCard() && (arg0.getSource() == trekkOppdrag || arg0.getSource() == bygg)) {
            	JOptionPane.showMessageDialog(null, "Du har allereie trekt eitt kort. Da kan du ikkje bygge eller trekke oppdrag, du må trekke eitt kort til.");
                return;
            }

            if (arg0.getSource() == trekkOppdrag) {
            	hovud.trekkOppdrag();
            }
            else if (arg0.getSource() == bygg) {
                new BuildRouteHandler(hovud.findRoutesNotYetBuilt(), hovud.findPlayerInAction().getNumberOfRemainingJokers(), hovud);
            }
            else if (arg0.getSource() == visMineKort) {
                new ShowMyCardsHandler(hovud.findPlayerInAction());
            }
            else if (arg0.getSource() == visMineOppdrag) {
                new ShowMyMissionsHandler(hovud.findPlayerInAction());
            }
            else if (arg0.getSource() == visBygde) {
                new ShowBuiltRoutesHandler(hovud.getAllBuiltRoutes());
            }
        }
        catch (RemoteException e2) {
            e2.printStackTrace();
        }
    }
}