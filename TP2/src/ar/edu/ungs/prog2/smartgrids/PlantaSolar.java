
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaSolar extends PlantaRenovable {

    private double superficieCubierta;
    private int cantidadPaneles;
    private double horasRadiacionDiaria;

    public PlantaSolar(
            String nombre,
            String direccion,
            double capacidadMaxima,
            LocalDateTime fechaPuesta,
            String fuentePrimaria,
            double eficienciaPromedio,
            double superficieCubierta,
            int cantidadPaneles,
            double horasRadiacionDiaria) {

        super(
            nombre,
            direccion,
            capacidadMaxima,
            fechaPuesta,
            fuentePrimaria,
            eficienciaPromedio
        );

        if (superficieCubierta <= 0) {
            throw new RuntimeException(
                "La superficie cubierta debe ser positiva"
            );
        }

        if (cantidadPaneles <= 0) {
            throw new RuntimeException(
                "La cantidad de paneles debe ser positiva"
            );
        }

        if (horasRadiacionDiaria < 0 || horasRadiacionDiaria > 24) {
            throw new RuntimeException(
                "Las horas de radiación deben estar entre 0 y 24"
            );
        }

        this.superficieCubierta = superficieCubierta;
        this.cantidadPaneles = cantidadPaneles;
        this.horasRadiacionDiaria = horasRadiacionDiaria;
    }

    public double getSuperficieCubierta() {
        return superficieCubierta;
    }

    public int getCantidadPaneles() {
        return cantidadPaneles;
    }

    public double getHorasRadiacionDiaria() {
        return horasRadiacionDiaria;
    }

    @Override
    public double capacidadMinimaCuota() {
        return getCapacidadMaxima()
            * getEficienciaPromedio()
            * horasRadiacionDiaria / 24.0;
    }

    @Override
    public boolean cumpleCuota(RegistroMonitoreo monitoreo) {
        return monitoreo.getEnergiaGenerada() >= capacidadMinimaCuota();
    }

    @Override
    public String toString() {
        return super.toString()
            + ", Superficie cubierta: " + superficieCubierta
            + ", Cantidad de paneles: " + cantidadPaneles
            + ", Horas de radiación diaria: " + horasRadiacionDiaria;
    }
}
