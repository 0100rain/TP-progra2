
package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaEolica extends PlantaRenovable {

    private int cantidadAerogeneradores;
    private double alturaTorres;
    private double velocidadMinimaViento;

    public PlantaEolica(
            String nombre,
            String direccion,
            double capacidadMaxima,
            LocalDateTime fechaPuesta,
            String fuentePrimaria,
            double eficienciaPromedio,
            int cantidadAerogeneradores,
            double alturaTorres,
            double velocidadMinimaViento) {

        super(
            nombre,
            direccion,
            capacidadMaxima,
            fechaPuesta,
            fuentePrimaria,
            eficienciaPromedio
        );

        if (cantidadAerogeneradores <= 0) {
            throw new RuntimeException(
                "La cantidad de aerogeneradores debe ser positiva"
            );
        }

        if (alturaTorres <= 0) {
            throw new RuntimeException(
                "La altura de las torres debe ser positiva"
            );
        }

        if (velocidadMinimaViento < 0) {
            throw new RuntimeException(
                "La velocidad mínima del viento no puede ser negativa"
            );
        }

        this.cantidadAerogeneradores = cantidadAerogeneradores;
        this.alturaTorres = alturaTorres;
        this.velocidadMinimaViento = velocidadMinimaViento;
    }

    public int getCantidadAerogeneradores() {
        return cantidadAerogeneradores;
    }

    public double getAlturaTorres() {
        return alturaTorres;
    }

    public double getVelocidadMinimaViento() {
        return velocidadMinimaViento;
    }

    @Override
    public double capacidadMinimaCuota() {
        return getCapacidadMaxima() * getEficienciaPromedio();
    }

    @Override
    public boolean cumpleCuota(RegistroMonitoreo monitoreo) {
        return monitoreo instanceof MonitoreoEolico
            && monitoreo.getEnergiaGenerada() >= capacidadMinimaCuota()
            && ((MonitoreoEolico) monitoreo).getVelocidadViento()
                >= velocidadMinimaViento;
    }

    @Override
    public String toString() {
        return super.toString()
            + ", Cantidad de aerogeneradores: " + cantidadAerogeneradores
            + ", Altura de las torres: " + alturaTorres
            + ", Velocidad mínima del viento: " + velocidadMinimaViento;
    }
}
