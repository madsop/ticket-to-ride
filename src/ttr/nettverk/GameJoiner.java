package ttr.nettverk;

import java.awt.HeadlessException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import ttr.data.Colour;
import ttr.data.Konstantar;
import ttr.gui.GUI;
import ttr.kjerna.Core;
import ttr.spelar.IPlayer;

public class GameJoiner {
	private Colour[] cardsOnHostTable;
	private GUI gui;
	private Core core;
	private String port;

	public GameJoiner(GUI gui, Core core, String port) {
		this.gui = gui;
		this.core = core;
		this.port = port;
		this.cardsOnHostTable = new Colour[Konstantar.ANTAL_KORT_PÅ_BORDET];
	}

	boolean joinGame() throws HeadlessException {
		String url = tryToJoinGame(findRemoteAddress());

		try {
			actuallyJoinGame(url);
			return true;
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(gui, "Klarte dessverre ikkje å bli med i spelet.");
			e.printStackTrace();
			return false;
		}
	}	

	private String findRemoteAddress() {
		String remoteAddress = JOptionPane.showInputDialog("Kven vil du kople deg til? (IP-adresse eller hostnamn)");
		if (remoteAddress.equals("") || remoteAddress.length()==0) {
			remoteAddress = "localhost"; 
		}
		System.out.println(remoteAddress);
		return remoteAddress;
	}

	private String tryToJoinGame(String remoteAddress){
		System.setSecurityManager(new LiberalSecurityManager());
		String url = "rmi://" + remoteAddress + ":" + port + "/ISpelar"; // URL-en til verten i RMI-registeret.
		System.out.println(url);

		if (core.getSpelarar().size() == Konstantar.MAKS_ANTAL_SPELARAR) {
			JOptionPane.showMessageDialog(gui, "Synd, men det kan ikkje vera med fleire spelarar enn dei som no spelar. Betre lukke neste gong!");
			System.exit(0);
		}
		return url;
	} 

	private void actuallyJoinGame(String url) throws NotBoundException, 	MalformedURLException, RemoteException {
		IPlayer host = (IPlayer)Naming.lookup(url);
		core.getMinSpelar().registrerKlient(host); 
		for (IPlayer player : core.getSpelarar()) {
			faaMedSpelar(player);
		}

		core.getMinSpelar().setPaaBord(cardsOnHostTable);
		updateOtherPlayers(host);
		core.settSinTur(host);
	}

	private void faaMedSpelar(IPlayer player) throws RemoteException{
		if (player.getSpelarNummer() == 0) {
			registerHost(player);
		}
		player.receiveMessage(core.getMinSpelar().getNamn() + " har vorti med i spelet.");
		if (player.getSpelarNummer()!=0) {
			gui.receiveMessage(player.getNamn() + " er òg med i spelet.");
		}
		player.registrerKlient(core.getMinSpelar());
	}

	private void registerHost(IPlayer player) throws RemoteException {
		core.getMinSpelar().setPlayerNumberAndUpdatePlayerCounter(player.getSpelarteljar());
		gui.receiveMessage(player.getNamn() +" er vert for spelet.");
		cardsOnHostTable = player.getCardsOnTable();
	}

	private void updateOtherPlayers(IPlayer host) throws RemoteException {
		for (IPlayer player : host.getSpelarar()) {
			if (!(player.getNamn().equals(core.getMinSpelar().toString()))) {
				//hovud.getSpelarar().add(s);
				core.getMinSpelar().registrerKlient(player);
				player.receiveMessage(core.getMinSpelar().getNamn() + " har vorti med i spelet.");
				gui.receiveMessage(player.getNamn() + " er òg med i spelet.");
				player.registrerKlient(core.getMinSpelar());
			}
		}
	}
}
