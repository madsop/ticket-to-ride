package ttr.spelar;

import java.awt.Component;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JOptionPane;

import ttr.bord.IBord;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;
import ttr.rute.IRute;

public class PlayerImpl extends UnicastRemoteObject {
	private static final long serialVersionUID = -6844537139622798129L;
	protected IHovud hovud;
	protected IBord bord;

	private static int spelarteljar = 0;    //TODO bør flyttes vekk. Kanskje til hovud.
	private int spelarNummer;
	private String namn;
	protected ArrayList<IRute> bygdeRuter; // Delvis unaudsynt pga. harEgBygdMellomAogB

	private boolean einValdAllereie;


	public PlayerImpl (IHovud hovud, String namn, IBord bord) throws RemoteException{
		super();
		this.hovud = hovud;
		this.bord = bord;
		this.namn = namn;
		einValdAllereie = false;
		bygdeRuter = new ArrayList<>();
	}

	public void setEittKortTrektInn(boolean b) {
		einValdAllereie = b;
	}
	public int getSpelarNummer() {
		return spelarNummer;
	}
	public boolean getValdAllereie() {
		return einValdAllereie;
	}

	public void setTogAtt(int plass, int tog) {
		hovud.getGui().getTogAtt()[plass].setText(String.valueOf(tog));
	}
	public void setSpelarNummer(int nummer) { spelarNummer = nummer; }
	public int getSpelarteljar() { return spelarteljar; }
	public void setSpelarteljar(int teljar) { spelarteljar = teljar; }
	public int getBygdeRuterSize() { return bygdeRuter.size(); }
	public int getBygdeRuterId(int j) { return bygdeRuter.get(j).getRuteId(); }

	public String getNamn() { return namn; }

	public int getGjenverandeTog() {
		int brukteTog = bygdeRuter.stream().mapToInt(x -> x.getLengde()).sum();
		return Konstantar.ANTAL_TOG - brukteTog;
	}

	@Override
	public String toString() { return namn; }



	public void nybygdRute(int ruteId, ISpelar byggjandeSpelar) {
		IRute vald = getRoute(ruteId).get();
		vald.setBygdAv(byggjandeSpelar);
		hovud.getAlleBygdeRuter().add(vald);
	}

	private Optional<IRute> getRoute(int ruteId) {
		return hovud.getRuter().stream().filter(f -> f.getRuteId()==ruteId).findAny();
	}

	public int[] getPaaBordetInt() {
		int[] bord = new int[Konstantar.ANTAL_KORT_PÅ_BORDET];

		for (int i = 0; i < hovud.getBord().getPaaBordet().length; i++) {
			for (int f = 0; f < Konstantar.FARGAR.length; f++) {
				if (hovud.getBord().getPaaBordet()[i] == Konstantar.FARGAR[f]) {
					bord[i] = f;
				}
			}
		}

		return bord;
	}


	public void visSpeletErFerdigmelding(String melding) {
		JOptionPane.showMessageDialog((Component) hovud.getGui(), melding);
	}

}