package ttr.copy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

public class Konstantar {
	public static final Farge[] FARGAR = {Farge.blå, Farge.raud, Farge.kvit, Farge.svart, 
		Farge.gul, Farge.grønn, Farge.oransje, Farge.lilla, Farge.valfri};
	public static final int ANTAL_KORT_PÅ_BORDET = 5;
	public static final int ANTAL_AV_KVART_FARGEKORT = 12;
	public static final int ANTAL_STARTOPPDRAG = 5;
	public static final int ANTAL_STARTKORT = 4;
	public static final int ANTAL_FARGAR = 9;
	public static final int ANTAL_DESTINASJONAR = 37;
	public static final int ANTAL_TOG = 40;
	public static final int MAKS_JOKRAR_PAA_BORDET = 1;
	public static final int MAKS_ANTAL_SPELARAR = 3;
	
	// GUI
	public static final int HOGDE = 750;
	public static final int BREIDDE = 1130;
	public static final int hogrebreidde = BREIDDE/3;
	public static final Dimension KNAPP = new Dimension(150,50);
	public static final Dimension KORTKNAPP = new Dimension(70,100);
	public static final Dimension SPELARNAMNDIM = new Dimension(350,30);
	public static final Font TOGTALFONT = new Font(Font.SERIF,Font.BOLD,20);
	public static final Insets INSETS = new Insets(10,10,10,10);
	
	/**
	 * Gjer om frå farge til Color. Merk: joker (valfri) blir cyan
	 * @param farge - kva for farge ein skal gjera om
	 * @return - fargen i Color
	 */
	public static final Color fargeTilColor(Farge farge) {
		Color ret = null;
		switch (farge) {
		case blå:
			ret = Color.BLUE;
			break;
		case gul:
			ret =  Color.YELLOW;
			break;
		case raud:
			ret = Color.RED;
			break;
		case grønn:
			ret = Color.GREEN;
			break;
		case kvit:
			ret = Color.WHITE;
			break;
		case lilla:
			ret = Color.PINK;
			break;
		case oransje:
			ret = Color.ORANGE;
			break;
		case svart:
			ret = Color.BLACK;
			break;
		case valfri:
			ret = Color.CYAN;
			break;
		default:
			ret = Color.BLACK;
			break;
		}
		return ret;
	}
	
}
