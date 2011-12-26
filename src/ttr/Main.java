package ttr;

import ttr.bord.Bord;
import ttr.bord.IBord;
import ttr.gui.GUI;
import ttr.gui.IGUI;
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
		JFrame frame = new JFrame("Ticket to ride");
		String arg;
		if (args.length < 1) { arg = "localhost"; } // If there are no arguments passed, we choose localhost as default.
		else { arg = args[0]; }
        
        ISpelUtgaave spel = velSpel(frame);

        boolean nett = (JOptionPane.showConfirmDialog(null, "Vil du spela eit nettverksspel?") == JOptionPane.YES_OPTION);
        IGUI gui = mekkGUI(spel,frame,nett);
        IBord bord = new Bord(gui,nett);
        IHovud hovud = new Hovud(gui, bord, nett, spel);
        gui.setHovud(hovud);

        hovud.settIGangSpelet(nett,arg);

	}
    
    private static ISpelUtgaave velSpel(JFrame frame){
        String valstring = "Vel Ticket to ride-utgåve";
        ISpelUtgaave[] spela = new ISpelUtgaave[2];
        spela[0] = new Nordic();
        spela[1] = new Europe();
        int spel = JOptionPane.showOptionDialog(frame, valstring, valstring, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, spela, spela[0]);
        if (!(spel >= 0 && spel < spela.length)){
            System.exit(0);
        }

        return spela[spel];
    }
    
    private static IGUI mekkGUI(ISpelUtgaave utgaave, JFrame frame, boolean nett) {
        IGUI gui = new GUI(frame,utgaave,nett);        // TODO: dependency injection
        frame.setTitle(frame.getTitle() + " - " +utgaave.getTittel());
        frame.setContentPane((Container) gui);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return gui;
    }
}