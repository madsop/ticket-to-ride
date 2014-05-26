package ttr.rute;

import ttr.data.Destination;
import ttr.data.Colour;
import ttr.spelar.PlayerAndNetworkWTF;

import java.io.Serializable;
import java.text.MessageFormat;

public class Route implements Serializable {
	private static final long serialVersionUID = -7151848709424908866L;
	private static final int[] valuesForRoute = {1,2,4,7,10,15,0,0,27};
	private final int length;
	private final Colour colour;
	private final boolean tunnel;
	private final int numberOfRequiredJokers;
	private PlayerAndNetworkWTF builtBy;
	private final Destination start;
	private final Destination end;

	public Route(@SuppressWarnings("unused") int routeId, Destination start, Destination end, int length, Colour colour, boolean tunnel, int numberOfRequiredJokers) {
		this(start, end, length, colour, tunnel, numberOfRequiredJokers);
	}

	public Route(Destination start, Destination end, int length, Colour colour, boolean tunnel, int numberOfRequiredJokers) {
		this.start = start;
		this.end = end;

		this.length = length;
		this.colour = colour;
		this.tunnel = tunnel;
		this.numberOfRequiredJokers = numberOfRequiredJokers;
	}

	public Destination getStart() {
		return start;
	}

	public Destination getEnd() {
		return end;
	}

	public int getValue() {
		return valuesForRoute[length-1];
	}

	public int getLength() {
		return length;
	}

	public Colour getColour() {
		return colour;
	}

	public boolean isTunnel() {
		return tunnel;
	}

	public int getNumberOfRequiredJokers() {
		return numberOfRequiredJokers;
	}

	public void setBuiltBy(PlayerAndNetworkWTF spelar) {
		builtBy = spelar;
	}

	public String toString() {
		String builtByString = "";
		if (builtBy != null) {
			builtByString = builtBy.getNamn();
		}
		String string = "{0} - {1}, lengde {2}, av farge {3}, tunnel? {4}, og {5} jokrar krevs for å byggje denne. Ruta er bygd av {6}, og er verdt {7} poeng.";
		return MessageFormat.format(string,	start, end, length, colour, tunnel,	numberOfRequiredJokers, builtByString, valuesForRoute[length-1]);

	}
}