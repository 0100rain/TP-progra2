
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public abstract class PlantaRenovable extends Planta {

    private String fuentePrimaria;
    private double eficienciaPromedio;

    public PlantaRenovable(
            String nombre,
            String direccion,
            double capacidadMaxima,
            LocalDateTime fechaPuesta,
            String fuentePrimaria,
            double eficienciaPromedio) {

        super(nombre, direccion, capacidadMaxima, fechaPuesta);

        if (fuentePrimaria == null || fuentePrimaria.trim().isEmpty()) {
            throw new RuntimeException(
                "La fuente primaria no puede estar vacía"
            );
        }

        if (eficienciaPromedio < 0 || eficienciaPromedio > 1) {
            throw new RuntimeException(
                "La eficiencia promedio debe estar entre 0 y 1"
            );
        }

        this.fuentePrimaria = fuentePrimaria;
        this.eficienciaPromedio = eficienciaPromedio;
    }

    public String getFuentePrimaria() {
        return fuentePrimaria;
    }

    public double getEficienciaPromedio() {
        return eficienciaPromedio;
    }

    @Override
    public String toString() {
        return super.toString()
            + ", Fuente primaria: " + fuentePrimaria
            + ", Eficiencia promedio: " + eficienciaPromedio;
    }
}
