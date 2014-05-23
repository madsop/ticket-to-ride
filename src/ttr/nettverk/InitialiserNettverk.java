package ttr.nettverk;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.gui.SwingUtils;
import ttr.kjerna.Core;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.spelar.PlayerNetworkClass;

import javax.swing.*;

import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class InitialiserNettverk {
	//TODO denne klassa treng refaktorisering frå h
	private final String hostAddress;
	private final String PORT = "1226";
	private final IGUI gui;
	private final Core hovud;
	private Farge[] paaVertBordet; //TODO korfor er denne int? Pga serialisering?

	public InitialiserNettverk(IGUI gui, String hostAddress, Core hovud) {
		this.hostAddress = hostAddress;
		this.gui = gui;
		this.hovud = hovud;
		paaVertBordet = new Farge[Konstantar.ANTAL_KORT_PÅ_BORDET];
	}

	public void initialiseNetworkGame() throws HeadlessException, RemoteException {
		PlayerAndNetworkWTF spelar = new PlayerNetworkClass(hovud,SwingUtils.showInputDialog("Skriv inn namnet ditt"),hovud.getTable());
		hovud.setMinSpelar(spelar);

		Object[] options = {"Nytt spel", "Bli med i spel"};
		int option = JOptionPane.showOptionDialog((Component) gui, "Start spel eller bli med i spel?", "Nytt spel", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); // Vel å starte spel
		if(option == 0){
			hostGame();
		}else if (option == 1){ // Vel å bli med i eit spel
			joinGame(findRemoteAddress());
		}
		else { // Vel å avbryte
			System.exit(0);
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

	void hostGame() throws HeadlessException, RemoteException {
		System.setSecurityManager(new LiberalSecurityManager());
		String address = hostAddress+":"+PORT;
		String url = "rmi://"+address+"/ISpelar"; // URL-en min i RMI-registeret.
		System.out.println(hovud.getMinSpelar().getNamn() +" er spelar nummer " 
				+hovud.getMinSpelar().getSpelarNummer());
		hovud.getTable().layFiveCardsOutOnTable();
		System.out.println(url);
		try {
			startHostingGame(url);
		} catch (Exception e) {
			JOptionPane.showMessageDialog((Component) gui, "Kunne ikkje starta spelet. Det er sikkert porten som er opptatt.");
			e.printStackTrace();
			initialiseNetworkGame(); // Vi prøver om att.
		}
	}

	private void startHostingGame(String url) throws RemoteException, MalformedURLException {
		LocateRegistry.createRegistry(Integer.parseInt(PORT));
		long time = System.currentTimeMillis();
		PlayerAndNetworkWTF meg = hovud.getMinSpelar();
		Naming.rebind(url, meg); // Legg til spelar i RMI-registeret
		time = System.currentTimeMillis() - time;
		System.out.println("Time to register with RMI registry: "+(time/1000)+"s");
		System.out.println("Spelet er starta, vent på at nokon skal kople seg til...");
		hovud.settSinTur(meg);
		meg.setSpelarNummer(0);
		meg.setSpelarteljar(1);
		gui.getMessagesModel().nyMelding(meg.getNamn() +" er vert for spelet.");
	}

	private String initJoin(String remoteAddress){
		System.setSecurityManager(new LiberalSecurityManager());
		String url = "rmi://"+remoteAddress+":"+PORT+"/ISpelar"; // URL-en til verten i RMI-registeret.
		System.out.println(url);

		if (hovud.getSpelarar().size()+1 >= Konstantar.MAKS_ANTAL_SPELARAR) {
			JOptionPane.showMessageDialog((Component) gui, "Synd, men det kan ikkje vera med fleire spelarar enn dei som no spelar. Betre lukke neste gong!");
			System.exit(0);
		}
		return url;

	} 

	private void faaMedSpelar(PlayerAndNetworkWTF player) throws RemoteException{
		if (player.getSpelarNummer() == 0) {
			registerHost(player);
		}

		player.receiveMessage(hovud.getMinSpelar().getNamn() + " har vorti med i spelet.");

		if (player.getSpelarNummer()!=0){
			gui.getMessagesModel().nyMelding(player.getNamn() + " er òg med i spelet.");
		}

		player.registrerKlient(hovud.getMinSpelar());
	}

	private void registerHost(PlayerAndNetworkWTF player) throws RemoteException {
		hovud.getMinSpelar().setSpelarNummer(player.getSpelarteljar());
		player.setSpelarteljar(player.getSpelarteljar()+1);
		gui.getMessagesModel().nyMelding(player.getNamn() +" er vert for spelet.");
		paaVertBordet = player.getCardsOnTable();
	}

	void oppdaterAndreSpelarar(PlayerAndNetworkWTF host) throws RemoteException{
		for (PlayerAndNetworkWTF player : host.getSpelarar()){
			if (!(player.getNamn().equals(hovud.getMinSpelar().toString()))){
				//hovud.getSpelarar().add(s);
				hovud.getMinSpelar().registrerKlient(player);
				player.receiveMessage(hovud.getMinSpelar().getNamn() + " har vorti med i spelet.");
				gui.getMessagesModel().nyMelding(player.getNamn() + " er òg med i spelet.");
				player.registrerKlient(hovud.getMinSpelar());
			}
		}
	}
	
	void ordnePåBordet() throws RemoteException {
		Farge[] paaBord = new Farge[paaVertBordet.length];
		for (int i = 0; i < paaBord.length; i++) {
			paaBord[i] = paaVertBordet[i];
		}
		hovud.getMinSpelar().setPaaBord(paaBord);
	}

	void joinGame(String remoteAddress) throws HeadlessException, RemoteException {
		String url = initJoin(remoteAddress);

		try {
			actuallyJoinGame(url);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog((Component) gui, "Klarte dessverre ikkje å bli med i spelet.");
			e.printStackTrace();
			initialiseNetworkGame(); // We try again
		}
	}

	private void actuallyJoinGame(String url) throws NotBoundException, 	MalformedURLException, RemoteException {
		PlayerAndNetworkWTF host = (PlayerAndNetworkWTF)Naming.lookup(url);
		hovud.getMinSpelar().registrerKlient(host); 
		for (PlayerAndNetworkWTF player : hovud.getSpelarar()) {
			faaMedSpelar(player);
		}

		ordnePåBordet();
		oppdaterAndreSpelarar(host);
		hovud.settSinTur(host);
	}
}