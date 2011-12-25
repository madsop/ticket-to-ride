package ttr;

import ttr.gui.GUI;
import ttr.gui.IGUI;
import ttr.utgaave.ISpelUtgaave;
import ttr.utgaave.europe.Europe;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class Main {
	private static JFrame frame;
	public static JFrame getFrame() {
		return frame;
	}

	public static void main(String args[]) throws RemoteException {
		frame = new JFrame("Ticket to ride");
		String arg;
		if (args.length < 1) { arg = "localhost"; } // If there are no arguments passed, we choose localhost as default.
		else { arg = args[0]; }
        
        ISpelUtgaave spel = velSpel();

        mekkGUI(spel,arg);
	}
    
    private static ISpelUtgaave velSpel(){
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
    
    private static void mekkGUI(ISpelUtgaave utgaave, String hostAdresse) throws RemoteException{
        IGUI gui = new GUI(frame,hostAdresse,utgaave);        // TODO: dependency injection
        frame.setTitle(frame.getTitle() + " - " +utgaave.getTittel());
        frame.setContentPane((Container) gui);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}