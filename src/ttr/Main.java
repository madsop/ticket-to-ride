package ttr;

import ttr.bord.Table;
import ttr.bygg.ByggHjelpar;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.*;
import ttr.gui.hogresida.Hogrepanelet;
import ttr.gui.hogresida.Meldingspanel;
import ttr.kjerna.Core;
import ttr.kjerna.LocalCore;
import ttr.kjerna.NetworkCore;
import ttr.oppdrag.MissionHandler;
import ttr.rute.RouteHandler;
import ttr.utgaave.GameVersion;
import ttr.utgaave.europe.Europe;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.rmi.RemoteException;

public class Main {
	private Injector injector;

	public static void main(String args[]) {
		try {
			new Main(args);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public Main(String args[]) throws RemoteException {
		this.injector = Guice.createInjector();

		boolean isNetworkGame = (JOptionPane.showConfirmDialog(null, Infostrengar.velOmNettverkEllerIkkje) == JOptionPane.YES_OPTION);
		Table table = injector.getInstance(Table.class);
		if (!isNetworkGame){ // TODO: Få generalisert bort denne if-en
			table.layFiveCardsOutOnTable();
		}
		ByggHjelpar buildingHelper = injector.getInstance(ByggHjelpar.class);

		GameVersion gameVersion = chooseGameVersion();
		MissionHandler missionHandler = new MissionHandler(gameVersion.getOppdrag(), 
				new MissionChooserViewController(gameVersion, injector.getInstance(MissionChooserModel.class)));
		RouteHandler routeHandler = new RouteHandler(gameVersion);

		GUI gui = setUpGUI(gameVersion);
		Core core = isNetworkGame ? 
				new NetworkCore(gui, table, gameVersion, buildingHelper, missionHandler, routeHandler) : 
				new LocalCore(gui, table, gameVersion, buildingHelper, missionHandler, routeHandler);
		gui.setHovud(missionHandler, core);
		core.settIGangSpelet(getHostName(args));
	}

	private String getHostName(String[] args) {
		if (args.length < 1) { return Infostrengar.standardHostForNettverk; }
		return args[0];
	}

	private GameVersion chooseGameVersion() {
		GameVersion[] gameVersions = new GameVersion[2];
		gameVersions[0] = injector.getInstance(Nordic.class);
		gameVersions[1] = injector.getInstance(Europe.class);
		int chosenGameID = JOptionPane.showOptionDialog(null, Infostrengar.velUtgåve, Infostrengar.velUtgåve,
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameVersions, gameVersions[0]);
		if (chosenGameID < 0 || chosenGameID >= gameVersions.length){ System.exit(0); }
		return gameVersions[chosenGameID];
	}

	private GUI setUpGUI(GameVersion gameVersion) {
		ImagePanel picturePanel = new ImagePanel(gameVersion);
		
		GUI gui = new GUI(picturePanel, injector.getInstance(Meldingspanel.class), injector.getInstance(Hogrepanelet.class));        // TODO: dependency injection	
		setUpJFrame(gameVersion, gui);
		return gui;
	}

	private void setUpJFrame(GameVersion utgaave, GUI gui) {
		JFrame frame = new JFrame(Infostrengar.rammetittel + " - " +utgaave);
		frame.setPreferredSize(Konstantar.VINDUSSTORLEIK);
		frame.setContentPane(gui);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}