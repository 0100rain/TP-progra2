
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class MonitoreoTermico extends RegistroMonitoreo {

    private double co2Emitido;
    private double combustibleUtilizado;

    public MonitoreoTermico(
            LocalDateTime fechaHora,
            double energiaGenerada,
            int incidencias,
            double co2Emitido,
            double combustibleUtilizado) {

        super(fechaHora, energiaGenerada, incidencias);

        if (co2Emitido < 0) {
            throw new RuntimeException(
                "Las emisiones de CO2 no pueden ser negativas"
            );
        }

        if (combustibleUtilizado < 0) {
            throw new RuntimeException(
                "El combustible utilizado no puede ser negativo"
            );
        }

        this.co2Emitido = co2Emitido;
        this.combustibleUtilizado = combustibleUtilizado;
    }

    public double getCo2Emitido() {
        return co2Emitido;
    }

    public double getCombustibleUtilizado() {
        return combustibleUtilizado;
    }

    @Override
    public String toString() {
        return super.toString()
            + ", CO2 emitido: " + co2Emitido
            + ", Combustible utilizado: " + combustibleUtilizado;
    }
}
