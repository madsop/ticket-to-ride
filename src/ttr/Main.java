package ttr;

import ttr.bord.TableImpl;
import ttr.bord.Deck;
import ttr.bord.Table;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.*;
import ttr.gui.hogresida.Hogrepanelet;
import ttr.gui.hogresida.IHogrepanelet;
import ttr.gui.hogresida.IMeldingspanel;
import ttr.gui.hogresida.Meldingspanel;
import ttr.kjerna.Hovud;
import ttr.kjerna.IHovud;
import ttr.utgaave.GameVersion;
import ttr.utgaave.europe.Europe;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;

public class Main {
	public static void main(String args[]) throws RemoteException {
		JFrame frame = new JFrame(Infostrengar.rammetittel);
		       
        GameVersion gameVersion = chooseGameVersion(frame);

        boolean isNetworkGame = (JOptionPane.showConfirmDialog(null, Infostrengar.velOmNettverkEllerIkkje) == JOptionPane.YES_OPTION);
        IGUI gui = setUpGUI(gameVersion,frame,isNetworkGame);
        Table table = new TableImpl(gui,isNetworkGame, new Deck());
        IHovud hovud = new Hovud(gui, table, isNetworkGame, gameVersion);
        gui.setHovud(hovud);

        hovud.settIGangSpelet(isNetworkGame,getHostName(args));

	}

	private static String getHostName(String[] args) {
		if (args.length < 1) { return Infostrengar.standardHostForNettverk; }
		return args[0];
	}
    
    private static GameVersion chooseGameVersion(JFrame frame) {
        GameVersion[] gameVersions = new GameVersion[2];
        gameVersions[0] = new Nordic();
        gameVersions[1] = new Europe();
        int chosenGameID = JOptionPane.showOptionDialog(frame, Infostrengar.velUtgåve, Infostrengar.velUtgåve,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameVersions, gameVersions[0]);
        if (chosenGameID < 0 || chosenGameID >= gameVersions.length){ System.exit(0); }
        return gameVersions[chosenGameID];
    }
    
    private static IGUI setUpGUI(GameVersion gameVersion, JFrame frame, boolean isNetworkGame) {
        IBildePanel picturePanel = new BildePanel(gameVersion);

        MissionChooser missionChoose = new MissionChooserImpl(gameVersion,frame);

        IMeldingspanel messagepanel = new Meldingspanel(isNetworkGame);
        IHogrepanelet rightpanel = new Hogrepanelet(frame);
        IGUI gui = new GUI(picturePanel,missionChoose,messagepanel, rightpanel);        // TODO: dependency injection
        rightpanel.setGUI(gui);	
        setUpJFrame(gameVersion, frame, gui);
        return gui;
    }

	private static void setUpJFrame(GameVersion utgaave, JFrame frame, IGUI gui) {
		frame.setTitle(frame.getTitle() + " - " +utgaave);
        frame.setPreferredSize(Konstantar.VINDUSSTORLEIK);
        frame.setContentPane((Container) gui);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
}