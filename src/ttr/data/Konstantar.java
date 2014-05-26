package ttr.data;

import java.awt.*;

public class Konstantar {
	public static final Colour[] FARGAR = {Colour.blå, Colour.raud, Colour.kvit, Colour.svart,
		Colour.gul, Colour.grønn, Colour.oransje, Colour.lilla, Colour.valfri};
	public static final int ANTAL_KORT_PÅ_BORDET = 5;
	public static final int ANTAL_AV_KVART_FARGEKORT = 12;
	public static final int ANTAL_STARTOPPDRAG = 5;
	public static final int ANTAL_STARTKORT = 4;
	public static final int ANTAL_TOG = 40;
	public static final int MAKS_JOKRAR_PAA_BORDET = 2;
	public static final int MAKS_ANTAL_SPELARAR = 3;
	public static final int AVSLUTT_SPELET = 3;
	public static final int ANTAL_VELJEOPPDRAG = 3;

	// GUI
	public static final int HOGDE = 650;
	public static final int BREIDDE = 1200;
	public static final int DIFF = 50;
	public static final int MELDINGSPANELBREIDDE = 200;
	public static final int MELDINGSPANEL = MELDINGSPANELBREIDDE+20;
	public static final Dimension VINDUSSTORLEIK = new Dimension(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
	public static final int hogrebreidde = BREIDDE/3;
	public static final Dimension KNAPP = new Dimension(150,40);
	public static final Dimension KORTKNAPP = new Dimension(70,90);
	public static final Dimension SPELARNAMNDIM = new Dimension(300,30);
	public static final Dimension CHATDIM = new Dimension(200,40);
	public static final Font TOGTALFONT = new Font(Font.SERIF,Font.BOLD,20);
	public static final Insets INSETS = new Insets(10,10,10,10);

	public static Color fargeTilColor(Colour farge) {
		switch (farge) {
		case blå:
			return Color.BLUE;
		case gul:
			return Color.YELLOW;
		case raud:
			return Color.RED;
		case grønn:
			return Color.GREEN;
		case kvit:
			return Color.WHITE;
		case lilla:
			return Color.PINK;
		case oransje:
			return Color.ORANGE;
		case svart:
			return Color.BLACK;
		case valfri:
			return Color.CYAN;
		default:
			return Color.BLACK;
		}
	}
}