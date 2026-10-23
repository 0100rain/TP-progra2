
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class MonitoreoGeneral extends RegistroMonitoreo {

    public MonitoreoGeneral(
            LocalDateTime fechaHora,
            double energiaGenerada,
            int incidencias) {

        super(fechaHora, energiaGenerada, incidencias);
    }
}
