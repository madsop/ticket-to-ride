package ttr.gui.hogresida;

import ttr.data.IMeldingarModell;
import ttr.kjerna.IHovud;

public interface IMeldingspanel {
    void setHovud(IHovud hovud);

    IMeldingarModell getMeldingarModell();
}
