package ttr.nettverk;

import ttr.Spelar;
import ttr.SpelarImpl;
import ttr.data.Farge;
import ttr.gui.GUI;
import ttr.gui.Konstantar;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Nettverk {

	/** The port used for RMI */
    private final String hostAddress;
	private final String PORT = "1226";
	private final GUI gui;

	public Nettverk(GUI gui, String hostAddress) {
		this.hostAddress = hostAddress;
		this.gui = gui;
	}

	public void initialiserSpel() throws HeadlessException, RemoteException {
		gui.getHovud().setMinSpelar(new SpelarImpl(gui.getHovud(),JOptionPane.showInputDialog(gui,"Skriv inn namnet ditt")));
		Object[] options = {"Nytt spel", "Bli med i spel"};
		int option = JOptionPane.showOptionDialog(gui, "Start spel eller bli med i spel?", "Nytt spel", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); // Vel å starte spel
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
		String url = "rmi://"+address+"/Spelar"; // URL-en min i RMI-registeret.
		System.out.println(gui.getHovud().getMinSpelar().getNamn() +" er spelar nummer " 
				+gui.getHovud().getMinSpelar().getSpelarNummer());
		gui.getHovud().getBord().leggUtFem();
		System.out.println(url);
		try {
			LocateRegistry.createRegistry(Integer.parseInt(PORT));
			long time = System.currentTimeMillis();
			Spelar meg = gui.getHovud().getMinSpelar();
			Naming.rebind(url, meg); // Legg til spelar i RMI-registeret
			time = System.currentTimeMillis() - time;
			System.out.println("Time to register with RMI registry: "+(time/1000)+"s");
			System.out.println("Spelet er starta, vent på at nokon skal kople seg til...");
			gui.getHovud().settSinTur(meg);
			meg.setSpelarNummer(0);
			meg.setSpelarteljar(1);
			gui.getMeldingarModell().nyMelding(meg.getNamn() +" er vert for spelet.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(gui, "Kunne ikkje starta spelet. Det er sikkert porten som er opptatt.");
			e.printStackTrace();
			initialiserSpel(); // Vi prøver om att.
		}
	}

	void joinGame(String remoteAddress) throws HeadlessException, RemoteException {
		System.setSecurityManager(new LiberalSecurityManager());
		String url = "rmi://"+remoteAddress+":"+PORT+"/Spelar"; // URL-en til verten i RMI-registeret.
		System.out.println(url);

		if (gui.getHovud().getSpelarar().size()+1 >= Konstantar.MAKS_ANTAL_SPELARAR) {
			JOptionPane.showMessageDialog(gui, "Synd, men det kan ikkje vera med fleire spelarar enn dei som no spelar. Betre lukke neste gong!");
			System.exit(0);
		}

		try {
			// Sei ifrå til host-spelaren
			Spelar join = (Spelar)Naming.lookup(url);

			gui.getHovud().getMinSpelar().registrerKlient(join); // Finn verten i RMI-registeret og registrér han som motstandaren min.
			int[] paaVertBordet = null;
			for (Spelar s : gui.getHovud().getSpelarar()) {

				if (s.getSpelarNummer() == 0) {
					gui.getHovud().getMinSpelar().setSpelarNummer(s.getSpelarteljar());
					s.setSpelarteljar(s.getSpelarteljar()+1);

					gui.getMeldingarModell().nyMelding(s.getNamn() +" er vert for spelet.");
					paaVertBordet = s.getPaaBordetInt();
				}
				s.faaMelding(gui.getHovud().getMinSpelar().getNamn() + " har vorti med i spelet.");

				if (s.getSpelarNummer()!=0){
					gui.getMeldingarModell().nyMelding(s.getNamn() + " er òg med i spelet.");
				}
				s.registrerKlient(gui.getHovud().getMinSpelar());
			}

			Farge[] paaBord = new Farge[paaVertBordet.length];
			for (int i = 0; i < paaBord.length; i++) {
				paaBord[i] = Konstantar.FARGAR[paaVertBordet[i]];
			}					
			gui.getHovud().getMinSpelar().setPaaBord(paaBord);
			
			for (Spelar s : join.getSpelarar()){
				if (!(s.getNamn().equals(gui.getHovud().getMinSpelar().getNamn()))){
					//gui.getHovud().getSpelarar().add(s);
					gui.getHovud().getMinSpelar().registrerKlient(s);
					s.faaMelding(gui.getHovud().getMinSpelar().getNamn() + " har vorti med i spelet.");
					gui.getMeldingarModell().nyMelding(s.getNamn() + " er òg med i spelet.");
					s.registrerKlient(gui.getHovud().getMinSpelar());
				}
			}
			gui.getHovud().settSinTur(join);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(gui, "Klarte dessverre ikkje å bli med i spelet.");
			e.printStackTrace();
			initialiserSpel(); // We try again
		}
	}
}