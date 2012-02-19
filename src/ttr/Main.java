package ttr;

import ttr.bord.Bord;
import ttr.bord.IBord;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.*;
import ttr.gui.hogresida.Hogrepanelet;
import ttr.gui.hogresida.IHogrepanelet;
import ttr.gui.hogresida.IMeldingspanel;
import ttr.gui.hogresida.Meldingspanel;
import ttr.kjerna.Hovud;
import ttr.kjerna.IHovud;
import ttr.utgaave.ISpelUtgaave;
import ttr.utgaave.europe.Europe;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class Main {
	public static void main(String args[]) throws RemoteException {
		JFrame frame = new JFrame(Infostrengar.rammetittel);
		String arg;
		if (args.length < 1) { arg = Infostrengar.standardHostForNettverk; } // If there are no arguments passed, we choose localhost as default.
		else { arg = args[0]; }
        
        ISpelUtgaave spel = velSpel(frame);

        boolean nett = (JOptionPane.showConfirmDialog(null, Infostrengar.velOmNettverkEllerIkkje) == JOptionPane.YES_OPTION);
        IGUI gui = mekkGUI(spel,frame,nett);
        IBord bord = new Bord(gui,nett);
        IHovud hovud = new Hovud(gui, bord, nett, spel);
        gui.setHovud(hovud);

        hovud.settIGangSpelet(nett,arg);

	}
    
    private static ISpelUtgaave velSpel(JFrame frame){
        ISpelUtgaave[] spela = new ISpelUtgaave[2];
        spela[0] = new Nordic();
        spela[1] = new Europe();
        int spel = JOptionPane.showOptionDialog(frame, Infostrengar.velUtgåve, Infostrengar.velUtgåve,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, spela, spela[0]);
        if (!(spel >= 0 && spel < spela.length)){
            System.exit(0);
        }

        return spela[spel];
    }
    
    private static IGUI mekkGUI(ISpelUtgaave utgaave, JFrame frame, boolean nett) {
        IBildePanel bp = new BildePanel(utgaave);

        IOppdragsveljar oppdragsveljar = new Oppdragsveljar(utgaave,frame);

        IMeldingspanel meldingsboks = new Meldingspanel(nett);
        IHogrepanelet hogre = new Hogrepanelet(frame);
        IGUI gui = new GUI(bp,oppdragsveljar,meldingsboks, hogre);        // TODO: dependency injection
        hogre.setGUI(gui);
        frame.setTitle(frame.getTitle() + " - " +utgaave.getTittel());
        frame.setPreferredSize(Konstantar.VINDUSSTORLEIK);
        frame.setContentPane((Container) gui);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return gui;
    }
}