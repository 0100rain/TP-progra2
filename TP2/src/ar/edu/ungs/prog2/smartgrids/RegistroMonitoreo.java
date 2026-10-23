
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public abstract class RegistroMonitoreo {

    private LocalDateTime fechaHora;
    private double energiaGenerada;
    private int incidencias;

    public RegistroMonitoreo(
            LocalDateTime fechaHora,
            double energiaGenerada,
            int incidencias) {

        if (fechaHora == null) {
            throw new RuntimeException(
                "La fecha y hora del monitoreo no pueden ser nulas"
            );
        }

        if (energiaGenerada < 0) {
            throw new RuntimeException(
                "La energía generada no puede ser negativa"
            );
        }

        if (incidencias < 0) {
            throw new RuntimeException(
                "La cantidad de incidencias no puede ser negativa"
            );
        }

        this.fechaHora = fechaHora;
        this.energiaGenerada = energiaGenerada;
        this.incidencias = incidencias;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public double getEnergiaGenerada() {
        return energiaGenerada;
    }

    public int getIncidencias() {
        return incidencias;
    }

    @Override
    public String toString() {
        return "Fecha y hora: " + fechaHora
            + ", Energía generada: " + energiaGenerada
            + ", Incidencias: " + incidencias;
    }
}
