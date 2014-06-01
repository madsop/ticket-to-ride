package ttr;

import ttr.bord.Table;
import ttr.bord.Deck;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.*;
import ttr.gui.hogresida.Hogrepanelet;
import ttr.gui.hogresida.Meldingspanel;
import ttr.kjerna.Core;
import ttr.kjerna.LocalCore;
import ttr.kjerna.NetworkCore;
import ttr.utgaave.GameVersion;
import ttr.utgaave.europe.Europe;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.rmi.RemoteException;

public class Main {
	private Injector injector;
	
	//TODO folk får no berre sine eigne meldingar... (iallfall i nettverksspel)
	
	public static void main(String args[]) {
		try {
			new Main(args);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public Main(String args[]) throws RemoteException {
		this.injector = Guice.createInjector();
		JFrame frame = new JFrame(Infostrengar.rammetittel);
	       
        GameVersion gameVersion = chooseGameVersion(frame);
        boolean isNetworkGame = (JOptionPane.showConfirmDialog(null, Infostrengar.velOmNettverkEllerIkkje) == JOptionPane.YES_OPTION);
        GUI gui = setUpGUI(gameVersion,frame);
        Table table = new Table(gui, isNetworkGame, injector.getInstance(Deck.class));
        Core core = isNetworkGame ? new NetworkCore(gui, table, gameVersion) : new LocalCore(gui, table, gameVersion);
        gui.setHovud(core.getMissionHandler(), core);
        core.settIGangSpelet(getHostName(args));
	}

	private String getHostName(String[] args) {
		if (args.length < 1) { return Infostrengar.standardHostForNettverk; }
		return args[0];
	}
    
    private GameVersion chooseGameVersion(JFrame frame) {
        GameVersion[] gameVersions = new GameVersion[2];
        gameVersions[0] = injector.getInstance(Nordic.class);
        gameVersions[1] = injector.getInstance(Europe.class);
        int chosenGameID = JOptionPane.showOptionDialog(frame, Infostrengar.velUtgåve, Infostrengar.velUtgåve,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameVersions, gameVersions[0]);
        if (chosenGameID < 0 || chosenGameID >= gameVersions.length){ System.exit(0); }
        return gameVersions[chosenGameID];
    }
    
    private GUI setUpGUI(GameVersion gameVersion, JFrame frame) {
        ImagePanel picturePanel = new ImagePanel(gameVersion);
        MissionChooserViewController missionChooser = new MissionChooserViewController(gameVersion,frame);

        Hogrepanelet rightpanel = new Hogrepanelet(frame);
        GUI gui = new GUI(picturePanel, missionChooser, injector.getInstance(Meldingspanel.class), rightpanel);        // TODO: dependency injection	
        setUpJFrame(gameVersion, frame, gui);
        return gui;
    }

	private void setUpJFrame(GameVersion utgaave, JFrame frame, GUI gui) {
		frame.setTitle(frame.getTitle() + " - " +utgaave);
        frame.setPreferredSize(Konstantar.VINDUSSTORLEIK);
        frame.setContentPane(gui);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
}