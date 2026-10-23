
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Planta {

    private String nombre;
    private String direccion;
    private double capacidadMaxima;
    private LocalDateTime fechaPuestaEnFuncionamiento;
    private boolean activa;

    private Map<LocalDateTime, RegistroMonitoreo> monitoreos;

    public Planta(String nombre, String direccion, double capacidadMaxima,
                  LocalDateTime fechaPuestaEnFuncionamiento) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new RuntimeException("La dirección es obligatoria");
        }
        if (capacidadMaxima <= 0) {
            throw new RuntimeException("La capacidad debe ser positiva");
        }
        if (fechaPuestaEnFuncionamiento == null) {
            throw new RuntimeException("La fecha de puesta en funcionamiento es obligatoria");
        }

        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadMaxima = capacidadMaxima;
        this.fechaPuestaEnFuncionamiento = fechaPuestaEnFuncionamiento;
        this.activa = true;
        this.monitoreos = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public LocalDateTime getFechaPuestaEnFuncionamiento() {
        return fechaPuestaEnFuncionamiento;
    }

    public boolean estaActiva() {
        return activa;
    }
    
     public boolean isActiva() {
        return activa;
    }

    public Map<LocalDateTime, RegistroMonitoreo> getMonitoreos() {
        return monitoreos;
    }

    public abstract double capacidadMinimaCuota();

    public abstract boolean cumpleCuota(RegistroMonitoreo monitoreo);

    public void agregarMonitoreo(RegistroMonitoreo monitoreo) {
        if (!activa) {
            throw new RuntimeException("No se puede monitorear una planta inactiva");
        }
        if (monitoreo.getFechaHora().isBefore(fechaPuestaEnFuncionamiento)) {
            throw new RuntimeException("El monitoreo es anterior a la puesta en funcionamiento");
        }
        if (monitoreos.containsKey(monitoreo.getFechaHora())) {
            throw new RuntimeException("Ya existe un monitoreo en esa fecha y hora");
        }

        monitoreos.put(monitoreo.getFechaHora(), monitoreo);
    }

    public double totalGenerado() {
        double total = 0;

        for (RegistroMonitoreo monitoreo : monitoreos.values()) {
            total += monitoreo.getEnergiaGenerada();
        }

        return total;
    }

    private PeriodoInactividad periodoActual;
    private final List<PeriodoInactividad> periodosInactividad = new ArrayList<>();

    public void desactivar(LocalDateTime fechaHora) {
        if (fechaHora == null) {
         throw new RuntimeException("La fecha de desactivación es obligatoria");
      }

        if (!activa) {
         return;
     }

        if (fechaHora.isBefore(fechaPuestaEnFuncionamiento)) {
          throw new RuntimeException("La desactivación es anterior a la puesta en funcionamiento");
    }

    activa = false;
    periodoActual = new PeriodoInactividad(fechaHora);
    periodosInactividad.add(periodoActual);
    }

    public void activar(LocalDateTime fechaHora) {
     if (fechaHora == null) {
         throw new RuntimeException("La fecha de activación es obligatoria");
    }

     if (activa) {
        return;
    }

    periodoActual.registrarReactivacion(fechaHora);
    periodoActual = null;
    activa = true;
    }

    public boolean estabaInactivaEn(LocalDateTime fechaHora) {
       for (PeriodoInactividad periodo : periodosInactividad) {
          if (periodo.estaInactivaEn(fechaHora)) {
            return true;
        }
    }
    return false;
    }

    public boolean estabaInactiva(LocalDateTime fechaHora) {
        return !activa;
    }

    @Override
    public String toString() {
        return nombre + (activa ? " (ACTIVA): " : " (INACTIVA): ")
                + fechaPuestaEnFuncionamiento;
    }
}
