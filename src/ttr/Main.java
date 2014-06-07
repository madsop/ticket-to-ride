package ttr;

import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.*;
import ttr.kjerna.Core;
import ttr.kjerna.CoreFactory;
import ttr.oppdrag.MissionHandler;
import ttr.utgaave.GameVersion;
import ttr.utgaave.europe.Europe;
import ttr.utgaave.nordic.Nordic;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
		
		GameVersion gameVersion = chooseGameVersion();
		MissionHandler missionHandler = new MissionHandler(gameVersion.getOppdrag(), 
				new MissionChooserViewController(gameVersion, injector.getInstance(MissionChooserModel.class)));

		ImagePanel picturePanel = new ImagePanel(gameVersion);
		
		GUI gui = injector.getInstance(GUIFactory.class).createGUI(picturePanel);
		setUpJFrame(gameVersion, gui);
		Core core = injector.getInstance(CoreFactory.class).createCore(gui, missionHandler, gameVersion, isNetworkGame);
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