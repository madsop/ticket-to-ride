package ttr.nettverk;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.spelar.PlayerNetworkClass;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class InitialiserNettverk {

    private final String hostAddress;
	private final String PORT = "1226";
	private final IGUI gui;
    private final IHovud hovud;
    private int[] paaVertBordet; //TODO korfor er denne int? Pga serialisering?

	public InitialiserNettverk(IGUI gui, String hostAddress, IHovud hovud) {
		this.hostAddress = hostAddress;
		this.gui = gui;
        this.hovud = hovud;
        paaVertBordet =new int[Konstantar.ANTAL_KORT_PÅ_BORDET];
	}

	public void initialiseNetworkGame() throws HeadlessException, RemoteException {
        PlayerAndNetworkWTF spelar = new PlayerNetworkClass(hovud,gui.showInputDialog("Skriv inn namnet ditt"),hovud.getTable());
		hovud.setMinSpelar(spelar);

		Object[] options = {"Nytt spel", "Bli med i spel"};
		int option = JOptionPane.showOptionDialog((Component) gui, "Start spel eller bli med i spel?", "Nytt spel", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); // Vel å starte spel
		if(option == 0){
			hostGame();
		}else if (option == 1){ // Vel å bli med i eit spel
			String remoteAddress = JOptionPane.showInputDialog("Kven vil du kople deg til? (IP-adresse eller hostnamn)");
			if (remoteAddress.equals("") || remoteAddress.length()==0) {
				remoteAddress = "localhost"; 
			}
			System.out.println(remoteAddress);
			joinGame(remoteAddress);
		}
		else { // Vel å avbryte
			System.exit(0);
		}
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
			gui.getMeldingarModell().nyMelding(meg.getNamn() +" er vert for spelet.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog((Component) gui, "Kunne ikkje starta spelet. Det er sikkert porten som er opptatt.");
			e.printStackTrace();
			initialiseNetworkGame(); // Vi prøver om att.
		}
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
    
    private void faaMedSpelar(PlayerAndNetworkWTF s) throws RemoteException{
        if (s.getSpelarNummer() == 0) {
            hovud.getMinSpelar().setSpelarNummer(s.getSpelarteljar());
            s.setSpelarteljar(s.getSpelarteljar()+1);

            gui.getMeldingarModell().nyMelding(s.getNamn() +" er vert for spelet.");
            paaVertBordet = s.getPaaBordetInt();
        }

        s.receiveMessage(hovud.getMinSpelar().getNamn() + " har vorti med i spelet.");

        if (s.getSpelarNummer()!=0){
            gui.getMeldingarModell().nyMelding(s.getNamn() + " er òg med i spelet.");
        }

        s.registrerKlient(hovud.getMinSpelar());
    }

    void oppdaterAndreSpelarar(PlayerAndNetworkWTF join) throws RemoteException{
        for (PlayerAndNetworkWTF s : join.getSpelarar()){
            if (!(s.getNamn().equals(hovud.getMinSpelar().toString()))){
                //hovud.getSpelarar().add(s);
                hovud.getMinSpelar().registrerKlient(s);
                s.receiveMessage(hovud.getMinSpelar().getNamn() + " har vorti med i spelet.");
                gui.getMeldingarModell().nyMelding(s.getNamn() + " er òg med i spelet.");
                s.registrerKlient(hovud.getMinSpelar());
            }
        }
    }
    void ordnePåBordet() throws RemoteException {

        Farge[] paaBord = new Farge[paaVertBordet.length];
        for (int i = 0; i < paaBord.length; i++) {
            paaBord[i] = Konstantar.FARGAR[paaVertBordet[i]];
        }
        hovud.getMinSpelar().setPaaBord(paaBord);

    }

	void joinGame(String remoteAddress) throws HeadlessException, RemoteException {
		String url = initJoin(remoteAddress);

		try {
			// Sei ifrå til host-spelaren
			PlayerAndNetworkWTF host = (PlayerAndNetworkWTF)Naming.lookup(url);

			hovud.getMinSpelar().registrerKlient(host); // Finn verten i RMI-registeret og registrér han som motstandaren min.
			for (PlayerAndNetworkWTF s : hovud.getSpelarar()) {
                faaMedSpelar(s);
			}

            ordnePåBordet();
            oppdaterAndreSpelarar(host);
			hovud.settSinTur(host);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog((Component) gui, "Klarte dessverre ikkje å bli med i spelet.");
			e.printStackTrace();
			initialiseNetworkGame(); // We try again
		}
	}
}