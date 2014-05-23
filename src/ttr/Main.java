package ttr;

import ttr.bord.BordImpl;
import ttr.bord.Deck;
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
		       
        ISpelUtgaave gameVersion = chooseGameVersion(frame);

        boolean isNetworkGame = (JOptionPane.showConfirmDialog(null, Infostrengar.velOmNettverkEllerIkkje) == JOptionPane.YES_OPTION);
        IGUI gui = setUpGUI(gameVersion,frame,isNetworkGame);
        IBord table = new BordImpl(gui,isNetworkGame, new Deck());
        IHovud hovud = new Hovud(gui, table, isNetworkGame, gameVersion);
        gui.setHovud(hovud);

        hovud.settIGangSpelet(isNetworkGame,getHostName(args));

	}

	private static String getHostName(String[] args) {
		if (args.length < 1) { return Infostrengar.standardHostForNettverk; }
		return args[0];
	}
    
    private static ISpelUtgaave chooseGameVersion(JFrame frame) {
        ISpelUtgaave[] gameVersions = new ISpelUtgaave[2];
        gameVersions[0] = new Nordic();
        gameVersions[1] = new Europe();
        int chosenGameID = JOptionPane.showOptionDialog(frame, Infostrengar.velUtgåve, Infostrengar.velUtgåve,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameVersions, gameVersions[0]);
        if (chosenGameID < 0 || chosenGameID >= gameVersions.length){ System.exit(0); }
        return gameVersions[chosenGameID];
    }
    
    private static IGUI setUpGUI(ISpelUtgaave gameVersion, JFrame frame, boolean isNetworkGame) {
        IBildePanel picturePanel = new BildePanel(gameVersion);

        IOppdragsveljar missionChoose = new Oppdragsveljar(gameVersion,frame);

        IMeldingspanel messagepanel = new Meldingspanel(isNetworkGame);
        IHogrepanelet rightpanel = new Hogrepanelet(frame);
        IGUI gui = new GUI(picturePanel,missionChoose,messagepanel, rightpanel);        // TODO: dependency injection
        rightpanel.setGUI(gui);	
        setUpJFrame(gameVersion, frame, gui);
        return gui;
    }

	private static void setUpJFrame(ISpelUtgaave utgaave, JFrame frame, IGUI gui) {
		frame.setTitle(frame.getTitle() + " - " +utgaave);
        frame.setPreferredSize(Konstantar.VINDUSSTORLEIK);
        frame.setContentPane((Container) gui);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
}