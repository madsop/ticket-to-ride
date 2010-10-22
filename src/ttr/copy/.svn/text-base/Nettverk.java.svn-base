package ttr.copy;

import java.awt.HeadlessException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.JOptionPane;

public class Nettverk {

	/** The port used for RMI */
	public String hostAddress;
	public final String PORT = "1226";
	private GUI gui;
	
	public Nettverk(GUI gui, String hostAddress) {
		this.hostAddress = hostAddress;
		this.gui = gui;
	}
	
	public void initialiserSpel() throws HeadlessException, RemoteException {
		gui.getHovud().setMinSpelar(new SpelarImpl(gui.getHovud(),JOptionPane.showInputDialog(gui,"Skriv inn namnet ditt")));
		Object[] options = {"Host game", "Join game"};
		int option = JOptionPane.showOptionDialog(gui, "Host game or join game?", "New game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); // Choosing host game
		if(option == 0){
			 hostGame();
		}else if (option == 1){ // Choosing join game
			String remoteAddress = JOptionPane.showInputDialog("Please enter remote IP or host name");
			if (remoteAddress == "" || remoteAddress == null || remoteAddress.length()==0) { 
				remoteAddress = "localhost"; 
			}
			System.out.println(remoteAddress);
			joinGame(remoteAddress);
		}
		else { // Choosing cancel
			System.exit(0);
		}
	}
	
	public void hostGame() throws HeadlessException, RemoteException {
		System.setSecurityManager(new LiberalSecurityManager());
		String address = hostAddress+":"+PORT;
		String url = "rmi://"+address+"/Spelar"; // URL of object in RMI registry
		System.out.println(gui.getHovud().getMinSpelar().getNamn() +" er spelar nummer " 
				+gui.getHovud().getMinSpelar().getSpelarNummer());
		gui.getHovud().getBord().leggUtFem();
		System.out.println(url);
		try {
			LocateRegistry.createRegistry(Integer.parseInt(PORT));
			long time = System.currentTimeMillis();
			Spelar meg = gui.getHovud().getMinSpelar();
			Naming.rebind(url, meg); // Add player to RMI registry
			time = System.currentTimeMillis() - time;
			System.out.println("Time to register with RMI registry: "+(time/1000)+"s");
			System.out.println("Game hosted, waiting for connections...");
			gui.getHovud().settSinTur(meg);
			meg.setSpelarNummer(0);
			meg.setSpelarteljar(1);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(gui, "Could not host game!");
			e.printStackTrace();
			initialiserSpel(); // Vi prøver om att.
		}
	}
	
	public void joinGame(String remoteAddress) throws HeadlessException, RemoteException {
		System.setSecurityManager(new LiberalSecurityManager());
		String url = "rmi://"+remoteAddress+":"+PORT+"/Spelar"; // URL of hosting player in RMI registry
		System.out.println(url);
		
		if (gui.getHovud().getSpelarar().size() >= Konstantar.MAKS_ANTAL_SPELARAR-1) {
			JOptionPane.showMessageDialog(gui, "Synd, men det kan ikkje vera med fleire spelarar enn dei som no spelar. Betre lukke neste gong!");
			System.exit(0);
		}
		
		try {
			// Sei ifrå til host-spelaren
			Spelar join = (Spelar)Naming.lookup(url);
			
			gui.getHovud().getMinSpelar().registrerKlient(join); // Find player in RMI registry and bind it as adversary to local player
			int[] paaVertBordet = null;
			for (Spelar s : gui.getHovud().getSpelarar()) {
				System.out.println("Spelarteljar: " +s.getSpelarteljar());
				
				if (s.getSpelarNummer() == 0) {
					gui.getHovud().getMinSpelar().setSpelarNummer(s.getSpelarteljar());
					System.out.println("Join: " +join.getNamn() + join.getSpelarNummer());
					System.out.println("MinSpelar: " +gui.getHovud().getMinSpelar().getNamn() + gui.getHovud().getMinSpelar().getSpelarNummer());
					s.setSpelarteljar(s.getSpelarteljar()+1);
					
					paaVertBordet = s.getPaaBordetInt();
				};
				s.registrerKlient(gui.getHovud().getMinSpelar());
			}
			
			Farge[] paaBord = new Farge[paaVertBordet.length];
			for (int i = 0; i < paaBord.length; i++) {
				paaBord[i] = Konstantar.FARGAR[paaVertBordet[i]];
			}					
			gui.getHovud().getMinSpelar().setPaaBord(paaBord);
			
			gui.getHovud().getSpelarar().add(join);
			
	/*		for (Spelar s : join.getSpelarar()){
	 			if (s!= gui.getHovud().getMinSpelar() {
					gui.getHovud().getSpelarar().add(s);
					gui.getHovud().getMinSpelar().registrerKlient(s);
				}
			}
	*/
			
			gui.getHovud().settSinTur(gui.getHovud().getSpelarar().get(0));
		}		
		catch (Exception e) {
			JOptionPane.showMessageDialog(gui, "Could not join game!");
			e.printStackTrace();
			initialiserSpel(); // We try again
		}
	}
}