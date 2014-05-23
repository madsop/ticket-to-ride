package ttr.utgaave.nordic;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.oppdrag.Mission;
import ttr.oppdrag.Mission;
import ttr.rute.Route;
import ttr.rute.Route;
import ttr.utgaave.AbstractGameVersion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public final class Nordic extends AbstractGameVersion {
	public static final String tittel = "Nordic countries";
	private static final String bilde = "nordic_map.jpg";
	
	public Nordic() {
		super(tittel,bilde);
	}

	protected Set<Route> leggTilRuter() {
		routes = new HashSet<>();
		int ruteid = 0;
		routes.add(new Route (ruteid,Destinasjon.Oslo,Destinasjon.Ørebro,2,Farge.gul,false,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Oslo,Destinasjon.Ørebro,2,Farge.grønn,false,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Ørebro,Destinasjon.Stockholm,2,Farge.lilla,false,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Ørebro,Destinasjon.Stockholm,2,Farge.svart,false,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Stockholm,Destinasjon.Norrkøping,1,Farge.oransje,false,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Stockholm,Destinasjon.Norrkøping,1,Farge.raud,false,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Stockholm,Destinasjon.Sundsvall,4,Farge.valfri,false,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Stockholm,Destinasjon.Sundsvall,4,Farge.valfri,false,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Sundsvall,Destinasjon.Østersund,2,Farge.grønn,false,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Østersund,Destinasjon.Trondheim,2,Farge.svart,false,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Trondheim,Destinasjon.MoIRana,5,Farge.grønn,true,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Trondheim,Destinasjon.MoIRana,6,Farge.raud,false,2));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Trondheim,Destinasjon.Åndalsnes,2,Farge.kvit,false,1));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Åndalsnes,Destinasjon.Bergen,5,Farge.svart,false,2));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Lillehammer,Destinasjon.Trondheim,3,Farge.oransje,true,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Lillehammer,Destinasjon.Oslo,2,Farge.lilla,true,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Lillehammer,Destinasjon.Åndalsnes,2,Farge.gul,true,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Oslo,Destinasjon.Bergen,4,Farge.raud,true,0));
		ruteid++;
		routes.add(new Route (ruteid,Destinasjon.Oslo,Destinasjon.Bergen,4,Farge.blå,true,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Stavanger,Destinasjon.Bergen,2,Farge.lilla,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Stavanger,Destinasjon.Kristiansand,2,Farge.grønn,true,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Stavanger,Destinasjon.Kristiansand,3,Farge.oransje,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Kristiansand,Destinasjon.Aalborg,2,Farge.raud,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Kristiansand,Destinasjon.Oslo,2,Farge.svart,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Oslo,Destinasjon.Aalborg,3,Farge.kvit,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Aalborg,Destinasjon.Århus,1,Farge.lilla,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Aalborg,Destinasjon.Gøteborg,1,Farge.valfri,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Århus,Destinasjon.København,1,Farge.valfri,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.København,Destinasjon.Gøteborg,2,Farge.svart,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Oslo,Destinasjon.Gøteborg,2,Farge.oransje,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.København,Destinasjon.Karlskrona,2,Farge.grønn,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.København,Destinasjon.Karlskrona,2,Farge.blå,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Norrkøping,Destinasjon.Karlskrona,3,Farge.kvit,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Norrkøping,Destinasjon.Karlskrona,3,Farge.gul,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Gøteborg,Destinasjon.Norrkøping,3,Farge.valfri,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Gøteborg,Destinasjon.Ørebro,2,Farge.blå,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Norrkøping,Destinasjon.Ørebro,2,Farge.valfri,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Stockholm,Destinasjon.Tallinn,4,Farge.grønn,false,2));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Tallinn,Destinasjon.Helsinki,2,Farge.lilla,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Stockholm,Destinasjon.Helsinki,4,Farge.valfri,false,2)); 
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Stockholm,Destinasjon.Helsinki,4,Farge.gul,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Stockholm,Destinasjon.Turku,3,Farge.blå,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Helsinki,Destinasjon.Turku,1,Farge.kvit,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Helsinki,Destinasjon.Tampere,1,Farge.oransje,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Helsinki,Destinasjon.Lahti,1,Farge.svart,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Turku,Destinasjon.Tampere,1,Farge.raud,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Tampere,Destinasjon.Lahti,1,Farge.blå,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Helsinki,Destinasjon.Imatra,3,Farge.raud,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Tampere,Destinasjon.Vaasa,2,Farge.lilla,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Lahti,Destinasjon.Imatra,2,Farge.gul,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Lahti,Destinasjon.Kuopio,3,Farge.kvit,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Vaasa,Destinasjon.Kuopio,4,Farge.valfri,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Imatra,Destinasjon.Kuopio,2,Farge.lilla,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Kuopio,Destinasjon.Lieksa,1,Farge.svart,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Kuopio,Destinasjon.Kajaani,2,Farge.grønn,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Kuopio,Destinasjon.Oulo,3,Farge.valfri,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Vaasa,Destinasjon.Oulo,3,Farge.svart,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Kajaani,Destinasjon.Oulo,2,Farge.gul,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Kajaani,Destinasjon.Lieksa,1,Farge.blå,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Kuopio,Destinasjon.Kajaani,2,Farge.grønn,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Lieksa,Destinasjon.Murmansk,9,Farge.valfri,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Oulo,Destinasjon.Tornio,1,Farge.kvit,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Oulo,Destinasjon.Rovaniemi,2,Farge.oransje,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Tornio,Destinasjon.Rovaniemi,1,Farge.raud,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Rovaniemi,Destinasjon.Kirkenes,5,Farge.blå,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Tornio,Destinasjon.Boden,1,Farge.grønn,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Boden,Destinasjon.Kiruna,3,Farge.oransje,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Boden,Destinasjon.Kiruna,3,Farge.svart,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Sundsvall,Destinasjon.Umeå,3,Farge.lilla,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Sundsvall,Destinasjon.Umeå,3,Farge.gul,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Sundsvall,Destinasjon.Vaasa,3,Farge.blå,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Sundsvall,Destinasjon.Ørebro,4,Farge.oransje,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Umeå,Destinasjon.Boden,3,Farge.raud,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Umeå,Destinasjon.Boden,3,Farge.kvit,false,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Umeå,Destinasjon.Vaasa,1,Farge.valfri,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.MoIRana,Destinasjon.Narvik,4,Farge.oransje,false,2));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Narvik,Destinasjon.Tromsø,3,Farge.gul,false,1));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Kiruna,Destinasjon.Narvik,1,Farge.lilla,true,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Kiruna,Destinasjon.Narvik,1,Farge.kvit,true,0));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Tromsø,Destinasjon.Honningsvåg,4,Farge.lilla,false,2));
		ruteid++; routes.add(new Route (ruteid,Destinasjon.Honningsvåg,Destinasjon.Kirkenes,2,Farge.grønn,false,1));
		ruteid++; routes.add(new Route(ruteid,Destinasjon.Kirkenes,Destinasjon.Murmansk,3,Farge.kvit,false,1));
		return routes;
	}

	protected ArrayList<Mission> fyllMedOppdrag() {
		missions = new ArrayList<>();
		int oppdragsid = 0;
		missions.add(new Mission(oppdragsid,Destinasjon.Oslo,Destinasjon.MoIRana,10));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Bergen,Destinasjon.København,9));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Narvik,Destinasjon.Tallinn,13));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Helsinki,Destinasjon.Lieksa,5));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Aalborg,Destinasjon.Norrkøping,5));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Helsinki,Destinasjon.København,10));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Helsinki,Destinasjon.Bergen,12));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Helsinki,Destinasjon.Østersund,8));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Bergen,Destinasjon.Trondheim,7));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Ørebro,Destinasjon.Kuopio,10));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Stockholm,Destinasjon.Bergen,8));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Turku,Destinasjon.Trondheim,10));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Stavanger,Destinasjon.Karlskrona,8));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Oslo,Destinasjon.Vaasa,9));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Oslo,Destinasjon.Stavanger,4));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Sundsvall,Destinasjon.Lahti,6));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Kristiansand,Destinasjon.MoIRana,12));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Stockholm,Destinasjon.Kajaani,10));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Oslo,Destinasjon.Stockholm,4));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Gøteborg,Destinasjon.Oulo,12));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Helsinki,Destinasjon.Kiruna,10));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Tornio,Destinasjon.Imatra,6));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.København,Destinasjon.Oulo,14));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Tampere,Destinasjon.Tallinn,3));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Tromsø,Destinasjon.Vaasa,11));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Narvik,Destinasjon.Murmansk,12));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Stockholm,Destinasjon.Imatra,7));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Gøteborg,Destinasjon.Turku,7));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Tampere,Destinasjon.Boden,6));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Aalborg,Destinasjon.Umeå,11));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Oslo,Destinasjon.Helsinki,8));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Helsinki,Destinasjon.Kirkenes,13));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Gøteborg,Destinasjon.Åndalsnes,6));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Stockholm,Destinasjon.Umeå,7));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Århus,Destinasjon.Lillehammer,6));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Stockholm,Destinasjon.København,6));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Norrkøping,Destinasjon.Boden,11));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Tampere,Destinasjon.Kristiansand,10));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Oslo,Destinasjon.Honningsvåg,21));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Bergen,Destinasjon.Tornio,17));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Bergen,Destinasjon.Narvik,16));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Stockholm,Destinasjon.Tromsø,17));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.København,Destinasjon.Murmansk,24));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.Stavanger,Destinasjon.Rovaniemi,18));
		oppdragsid++;
		missions.add(new Mission(oppdragsid,Destinasjon.København,Destinasjon.Narvik,18));
		return missions;
	}
}