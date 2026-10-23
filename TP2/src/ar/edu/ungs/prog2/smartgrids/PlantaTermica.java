
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaTermica extends Planta {

    private String tipoCombustible;
    private double objetivoEmisionesCo2PorHora;
    private double capacidadAlmacenamientoCombustible;

    public PlantaTermica(
            String nombre,
            String direccion,
            double capacidadMaxima,
            LocalDateTime fechaPuesta,
            String tipoCombustible,
            double objetivoEmisionesCo2PorHora,
            double capacidadAlmacenamientoCombustible) {

        super(nombre, direccion, capacidadMaxima, fechaPuesta);

        if (tipoCombustible == null || tipoCombustible.trim().isEmpty()) {
            throw new RuntimeException(
                "El tipo de combustible no puede estar vacío"
            );
        }

        if (objetivoEmisionesCo2PorHora < 0) {
            throw new RuntimeException(
                "El objetivo de emisiones de CO2 no puede ser negativo"
            );
        }

        if (capacidadAlmacenamientoCombustible <= 0) {
            throw new RuntimeException(
                "La capacidad de almacenamiento de combustible debe ser positiva"
            );
        }

        this.tipoCombustible = tipoCombustible;
        this.objetivoEmisionesCo2PorHora = objetivoEmisionesCo2PorHora;
        this.capacidadAlmacenamientoCombustible =
            capacidadAlmacenamientoCombustible;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public double getObjetivoEmisionesCo2PorHora() {
        return objetivoEmisionesCo2PorHora;
    }

    public double getCapacidadAlmacenamientoCombustible() {
        return capacidadAlmacenamientoCombustible;
    }

    @Override
    public double capacidadMinimaCuota() {
        return getCapacidadMaxima() / 2.0;
    }

    @Override
    public boolean cumpleCuota(RegistroMonitoreo monitoreo) {
        if (!(monitoreo instanceof MonitoreoTermico)) {
            return false;
        }

        MonitoreoTermico termico = (MonitoreoTermico) monitoreo;

        return termico.getEnergiaGenerada() >= capacidadMinimaCuota()
            && termico.getCo2Emitido() < objetivoEmisionesCo2PorHora;
    }

    @Override
    public String toString() {
        return super.toString()
            + ", Tipo de combustible: " + tipoCombustible
            + ", Objetivo de emisiones de CO2 por hora: "
            + objetivoEmisionesCo2PorHora
            + ", Capacidad de almacenamiento de combustible: "
            + capacidadAlmacenamientoCombustible;
    }
}
