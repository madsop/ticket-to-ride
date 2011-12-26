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
    private final JFrame frame;
    private final boolean nett;
    
    public WrapperKortListener(JButton kortBunke, JButton[] kortButtons, IHovud hovud, IGUI gui,JFrame frame, boolean nett){
        this.kortBunke = kortBunke;
        this.kortButtons = kortButtons;
        this.hovud = hovud;
        IGUI gui1 = gui;
        this.frame = frame;
        this.nett = nett;
    }

    public void actionPerformed(ActionEvent arg0) {


        if (arg0.getSource() == kortBunke) {
            new kortBunkeHandler(hovud);
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

    private void trekkInnEittKortFråBordet(int i,ISpelar kvenSinTur) throws RemoteException {
        Farge f = hovud.getBord().getPaaBordet()[i];
        if (kvenSinTur.getValdAllereie()) {
            if (f == Farge.valfri) {
                JOptionPane.showMessageDialog(frame, "Haha. Nice try. Du kan ikkje ta ein joker frå bordet når du allereie har trekt inn eitt kort");
                return;
            }
            else if (f==null){return;}
            else {
                kvenSinTur.faaKort(f);
                hovud.sendKortMelding(true,false,f);
                hovud.getBord().getTilfeldigKortFråBordet(i, true);
                hovud.nesteSpelar();
            }
        }
        else {
            if (f==null){return;}
            hovud.getKvenSinTur().faaKort(f);
            hovud.sendKortMelding(true,false,f);
            hovud.getBord().getTilfeldigKortFråBordet(i, true);
            if (f == Farge.valfri) {
                hovud.nesteSpelar();
            }
            hovud.getKvenSinTur().setEittKortTrektInn(true);
        }
    }

    private ISpelar orienterAndreSpelarar(int i) throws  RemoteException{
        ISpelar vert = null;
        if (nett){
            if (hovud.getMinSpelar().getSpelarNummer()==0) {
                vert = hovud.getMinSpelar();
            }
        }
        for (ISpelar s : hovud.getSpelarar()) {
            if (!nett){
                s.getTilfeldigKortFråBordet(i);
            }
            else {
                if (s.getSpelarNummer()==0) {
                    vert = s;
                }
            }
        }
        return vert;
    }

    /**
     * Lager ein knapp for å trekkje inn kort
     * @param i plassen på bordet
     * @throws RemoteException
     */
    void kortButton(int i) throws RemoteException {
        trekkInnEittKortFråBordet(i,hovud.getKvenSinTur());

        ISpelar vert = orienterAndreSpelarar(i);

        if (nett && vert!=null){
            Farge nyFarge = vert.getTilfeldigKortFråBordet(i);
            while (vert.sjekkJokrar()) {
                vert.leggUtFem();
                int[] paaSomInt = vert.getPaaBordetInt();

                for (int plass = 0; plass < hovud.getBord().getPaaBordet().length; plass++){
                    nyFarge = Konstantar.FARGAR[paaSomInt[plass]];
                    hovud.getMinSpelar().setPaaBordet(nyFarge,plass);
                    hovud.nyPaaPlass(vert, nyFarge, plass);
                }
            }
            hovud.nyPaaPlass(vert, nyFarge, i);
        }
    }
}