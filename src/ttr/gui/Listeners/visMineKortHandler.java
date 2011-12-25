package ttr.gui.Listeners;

import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class visMineKortHandler {
    public visMineKortHandler(IHovud hovud, IGUI gui){
        // vis korta mine
        JPanel korta = new JPanel();

        ISpelar visSine;
        if (hovud.isNett()) {
            visSine = hovud.getMinSpelar();
        }
        else {
            visSine = hovud.getKvenSinTur();
        }
        String[] kort = new String[Konstantar.ANTAL_FARGAR];

        for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++) {
            try {
                kort[i] = Konstantar.FARGAR[i] +": " +visSine.getKort()[i];
            } catch (RemoteException e) {
                kort[i] = "";
                e.printStackTrace();
            }
        }
        JLabel[] oppdr = new JLabel[kort.length];

        try {
            korta.add(new JLabel(visSine.getNamn()));
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

        for (int i = 0; i < oppdr.length; i++) {
            oppdr[i] = new JLabel();
            oppdr[i].setText(kort[i]);
            try {
                if (visSine.getKort()[i] != 0) {
                    oppdr[i].setForeground(Konstantar.fargeTilColor(Konstantar.FARGAR[i]));
                }
                else {
                    oppdr[i].setForeground(Color.LIGHT_GRAY);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            korta.add(oppdr[i]);
        }
        try {
            gui.lagRamme("Viser korta til " +visSine.getNamn(), korta);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
