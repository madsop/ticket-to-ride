package ttr.nettverk;

import java.awt.HeadlessException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.JOptionPane;

import ttr.gui.GUI;
import ttr.kjerna.Core;
import ttr.spelar.PlayerAndNetworkWTF;

public class GameHoster {
	private final String hostAddress;
	private String port;
	private GUI gui;
	private Core core;

	public GameHoster(String hostAddress, String port, GUI gui, Core core) {
		this.hostAddress = hostAddress;
		this.port = port;
		this.gui = gui;
		this.core = core;
	}

	boolean hostGame() throws HeadlessException, RemoteException {
		System.setSecurityManager(new LiberalSecurityManager());
		String url = "rmi://" + hostAddress+":" + port + "/ISpelar"; // URL-en min i RMI-registeret.
		System.out.println(core.getMinSpelar().getNamn() +" er spelar nummer " + core.getMinSpelar().getSpelarNummer());
		core.getTable().layFiveCardsOutOnTable(); //TODO dette må jo vera muleg å utnytte til å fjerne if-en frå table-konstruktøren
		System.out.println(url);
		try {
			startHostingGame(url);
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(gui, "Kunne ikkje starta spelet. Det er sikkert porten som er opptatt.");
			e.printStackTrace();
			return false;
		}
	}

	private void startHostingGame(String url) throws RemoteException, MalformedURLException {
		LocateRegistry.createRegistry(Integer.parseInt(port));
		long time = System.currentTimeMillis();
		PlayerAndNetworkWTF meg = (PlayerAndNetworkWTF) core.getMinSpelar();
		Naming.rebind(url, meg); // Legg til spelar i RMI-registeret
		time = System.currentTimeMillis() - time;
		System.out.println("Time to register with RMI registry: "+(time/1000)+"s");
		System.out.println("Spelet er starta, vent på at nokon skal kople seg til...");
		core.settSinTur(meg);
		meg.setPlayerNumberAndUpdatePlayerCounter(0);
		gui.receiveMessage(meg.getNamn() +" er vert for spelet.");
	}

}
