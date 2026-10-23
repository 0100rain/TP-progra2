
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaHidraulica extends PlantaRenovable {

    private int cantidadTurbinas;
    private double capacidadEmbalse;

    public PlantaHidraulica(
            String nombre,
            String direccion,
            double capacidadMaxima,
            LocalDateTime fechaPuesta,
            String fuentePrimaria,
            double eficienciaPromedio,
            int cantidadTurbinas,
            double capacidadEmbalse) {

        super(
            nombre,
            direccion,
            capacidadMaxima,
            fechaPuesta,
            fuentePrimaria,
            eficienciaPromedio
        );

        if (cantidadTurbinas <= 0) {
            throw new RuntimeException(
                "La cantidad de turbinas debe ser positiva"
            );
        }

        if (capacidadEmbalse <= 0) {
            throw new RuntimeException(
                "La capacidad del embalse debe ser positiva"
            );
        }

        this.cantidadTurbinas = cantidadTurbinas;
        this.capacidadEmbalse = capacidadEmbalse;
    }

    public int getCantidadTurbinas() {
        return cantidadTurbinas;
    }

    public double getCapacidadEmbalse() {
        return capacidadEmbalse;
    }

    @Override
    public double capacidadMinimaCuota() {
        return getCapacidadMaxima() * getEficienciaPromedio();
    }

    @Override
    public boolean cumpleCuota(RegistroMonitoreo monitoreo) {
        return monitoreo.getEnergiaGenerada() >= capacidadMinimaCuota();
    }

    @Override
    public String toString() {
        return super.toString()
            + ", Cantidad de turbinas: " + cantidadTurbinas
            + ", Capacidad del embalse: " + capacidadEmbalse;
    }
}
