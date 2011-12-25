package ttr.Listeners;

import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.kjerna.Oppdragshandsamar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class WrapperHogresideListener implements ActionListener{
    private final IGUI gui;
    private final IHovud hovud;
    private final JButton visBygde;
    private final JButton visMineKort;
    private final JButton visMineOppdrag;
    private final JButton trekkOppdrag;
    private final JButton bygg;
    private final JFrame frame;

    public WrapperHogresideListener(IGUI gui, IHovud hovud, JButton visBygde,
                                    JButton visMineKort,JButton visMineOppdrag,JButton trekkOppdrag, JButton bygg,
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
    
    private void trekkOppragHandler() {
        try {
            gui.sendKortMelding(false, false, Konstantar.FARGAR[0]);
            Oppdragshandsamar.trekkOppdrag(gui, hovud.getKvenSinTur(), false);
            hovud.nesteSpelar();
        }
        catch (RemoteException re) {
            re.printStackTrace();
        }
    }


    public void actionPerformed(ActionEvent arg0) {
        try {
            if (hovud.isNett() && (!hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())) ) {
                if (!(arg0.getSource() == visBygde || arg0.getSource() == visMineKort || arg0.getSource() == visMineOppdrag)) {
                    JOptionPane.showMessageDialog((Component) gui, "Det er ikkje din tur!");
                    return;
                }
            }
        } catch (HeadlessException e2) {
            e2.printStackTrace();
        } catch (RemoteException e2) {
            e2.printStackTrace();
        }

        if (arg0.getSource() == trekkOppdrag) {
            trekkOppragHandler();
        }
        else if (arg0.getSource() == bygg) {
            new ByggHandler(hovud,frame);
        }
        else if (arg0.getSource() == visMineKort) {
            new visMineKortHandler(hovud,gui);
        }

        else if (arg0.getSource() == visMineOppdrag) {
            new visMineOppdragHandler(hovud,gui);
        }

        else if (arg0.getSource() == visBygde) {
            new visBygdeHandler(hovud,gui,frame);
        }
    }
}
