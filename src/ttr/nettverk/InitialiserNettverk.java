package ttr.nettverk;

import ttr.gui.GUI;
import ttr.gui.SwingUtils;
import ttr.kjerna.Core;
import ttr.spelar.PlayerAndNetworkWTF;

import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;

public class InitialiserNettverk {
	private final String PORT = "1226";
	private final GUI gui;
	private final Core core;
	private GameJoiner gameJoiner;
	private GameHoster gameHoster;

	public InitialiserNettverk(GUI gui, String hostAddress, Core core) {
		this.gui = gui;
		this.core = core;
		this.gameHoster = new GameHoster(hostAddress, PORT, gui, core);
		this.gameJoiner = new GameJoiner(gui, core, PORT);
	}

	public void initialiseNetworkGame() throws HeadlessException, RemoteException {
		PlayerAndNetworkWTF spelar = new PlayerAndNetworkWTF(core, SwingUtils.showInputDialog("Skriv inn namnet ditt"), core.getTable());
		core.setMinSpelar(spelar);

		chooseBetweenHostAndJoin();
	}

	private void chooseBetweenHostAndJoin() throws RemoteException {
		Object[] options = {"Nytt spel", "Bli med i spel"};
		int option = JOptionPane.showOptionDialog(gui, "Start spel eller bli med i spel?", "Nytt spel", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); // Vel å starte spel
		if (option == 0) {
			hostGame();
		} else if (option == 1) {
			joinGame();
		}
		else {
			System.exit(0);
		}
	}

	private void hostGame() throws RemoteException {
		boolean successfullyHosting = gameHoster.hostGame();
		if(!successfullyHosting) {
			initialiseNetworkGame();
		}
	}

	private void joinGame() throws RemoteException {
		boolean successfullyJoining = gameJoiner.joinGame(); 
		if (!successfullyJoining) {
			initialiseNetworkGame();
		}
	}
}