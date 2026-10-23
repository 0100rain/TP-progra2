
package ar.edu.ungs.prog2.smartgrids;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class PeriodoInactividad {

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public PeriodoInactividad(LocalDateTime fechaInicio) {
        if (fechaInicio == null) {
            throw new RuntimeException(
                "La fecha de inicio de inactividad no puede ser nula"
            );
        }

        this.fechaInicio = fechaInicio;
        this.fechaFin = null;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public boolean estaAbierto() {
        return fechaFin == null;
    }

    public void registrarReactivacion(LocalDateTime fechaFin) {
        if (fechaFin == null) {
            throw new RuntimeException(
                "La fecha de reactivación no puede ser nula"
            );
        }

        if (!estaAbierto()) {
            throw new RuntimeException(
                "El período de inactividad ya está cerrado"
            );
        }

        if (fechaFin.isBefore(fechaInicio)) {
            throw new RuntimeException(
                "La reactivación no puede ser anterior a la desactivación"
            );
        }

        this.fechaFin = fechaFin;
    }

    public boolean estaInactivaEn(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new RuntimeException(
                "La fecha consultada no puede ser nula"
            );
        }

        boolean posteriorOIgualAlInicio =
            !fechaHora.isBefore(fechaInicio);

        boolean anteriorAlFin =
            fechaFin == null || fechaHora.isBefore(fechaFin);

        return posteriorOIgualAlInicio && anteriorAlFin;
    }

    @Override
    public String toString() {
        String fin = fechaFin == null
            ? "Sin reactivación"
            : fechaFin.toString();

        return "Inactividad desde " + fechaInicio + " hasta " + fin;
    }
}
