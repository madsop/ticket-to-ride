package ttr.Listeners;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class WrapperKortListener implements ActionListener{
    private final JButton kortBunke;
    private final JButton[] kortButtons;
    private final IHovud hovud;
    private final IGUI gui;
    private final JFrame frame;
    
    public WrapperKortListener(JButton kortBunke, JButton[] kortButtons, IHovud hovud, IGUI gui,JFrame frame){
        this.kortBunke = kortBunke;
        this.kortButtons = kortButtons;
        this.hovud = hovud;
        this.gui = gui;
        this.frame = frame;
    }
    

    @Override
    public void actionPerformed(ActionEvent arg0) {


        if (arg0.getSource() == kortBunke) {
            new kortBunkeHandler(hovud,gui);
        }

        else if (arg0.getSource() == kortButtons[0]) {
            try {
                kortButton(0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else if (arg0.getSource() == kortButtons[1]) {
            try {
                kortButton(1);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else if (arg0.getSource() == kortButtons[2]) {
            try {
                kortButton(2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else if (arg0.getSource() == kortButtons[3]) {
            try {
                kortButton(3);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else if (arg0.getSource() == kortButtons[4]) {
            try {
                kortButton(4);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lager ein knapp for å trekkje inn kort
     * @param i plassen på bordet
     * @throws RemoteException
     */
    void kortButton(int i) throws RemoteException {
        Farge f = hovud.getBord().getPaaBordet()[i];
        if (hovud.getKvenSinTur().getValdAllereie()) {
            if (f == Farge.valfri) {
                JOptionPane.showMessageDialog(frame, "Haha. Nice try. Du kan ikkje ta ein joker frå bordet når du allereie har trekt inn eitt kort");
                return;
            }
            else if (f==null){return;}
            else {
                hovud.getKvenSinTur().faaKort(f);
                gui.sendKortMelding(true,false,f);
                hovud.getBord().getTilfeldigKortFråBordet(i, true);
                hovud.nesteSpelar();
            }
        }
        else {
            if (f==null){return;}
            hovud.getKvenSinTur().faaKort(f);
            gui.sendKortMelding(true,false,f);
            hovud.getBord().getTilfeldigKortFråBordet(i, true);
            if (f == Farge.valfri) {
                hovud.nesteSpelar();
            }
            hovud.getKvenSinTur().setEinVald(true);
        }
        ISpelar vert = null;
        if (hovud.isNett()){
            if (hovud.getMinSpelar().getSpelarNummer()==0) {
                vert = hovud.getMinSpelar();
            }
        }
        for (ISpelar s : hovud.getSpelarar()) {
            if (!hovud.isNett()){
                s.getTilfeldigKortFråBordet(i);
            }
            else {
                if (s.getSpelarNummer()==0) {
                    vert = s;
                }
            }
        }
        if (hovud.isNett() && vert!=null){
            Farge nyFarge = vert.getTilfeldigKortFråBordet(i);
            while (vert.sjekkJokrar()) {
                vert.leggUtFem();
                int[] paaSomInt = vert.getPaaBordetInt();

                for (int plass = 0; plass < hovud.getBord().getPaaBordet().length; plass++){
                    nyFarge = Konstantar.FARGAR[paaSomInt[plass]];
                    hovud.getMinSpelar().setPaaBordet(nyFarge,plass);
                    gui.nyPaaPlass(vert, nyFarge, plass);
                }
            }
            gui.nyPaaPlass(vert, nyFarge, i);
        }
    }
}
