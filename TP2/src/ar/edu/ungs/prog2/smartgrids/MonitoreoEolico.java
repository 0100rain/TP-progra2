 
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class MonitoreoEolico extends RegistroMonitoreo {

    private double velocidadViento;

    public MonitoreoEolico(
            LocalDateTime fechaHora,
            double energiaGenerada,
            int incidencias,
            double velocidadViento) {

        super(fechaHora, energiaGenerada, incidencias);

        if (velocidadViento < 0) {
            throw new RuntimeException(
                "La velocidad del viento no puede ser negativa"
            );
        }

        this.velocidadViento = velocidadViento;
    }

    public double getVelocidadViento() {
        return velocidadViento;
    }

    @Override
    public String toString() {
        return super.toString()
            + ", Velocidad del viento: " + velocidadViento;
    }
}
