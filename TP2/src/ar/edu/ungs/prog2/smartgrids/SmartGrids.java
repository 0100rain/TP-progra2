
package ar.edu.ungs.prog2.smartgrids;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SmartGrids implements ISmartGrids {

    private Map<String, Planta> plantas;

    public SmartGrids() {
        plantas = new LinkedHashMap<>();
    }

    // --------------------------------------------------
    // MÉTODOS AUXILIARES
    // --------------------------------------------------

    private LocalDateTime parsearFechaHora(String fechaHora) {
        if (fechaHora == null || fechaHora.trim().isEmpty()) {
            throw new RuntimeException("La fecha y hora es obligatoria");
        }

        try {
            return LocalDateTime.parse(fechaHora);
        } catch (DateTimeParseException e) {
            throw new RuntimeException(
                "Formato de fecha y hora inválido: " + fechaHora
            );
        }
    }

    private LocalDate parsearFecha(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            throw new RuntimeException("La fecha es obligatoria");
        }

        try {
            return LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(fecha).toLocalDate();
            } catch (DateTimeParseException ex) {
                throw new RuntimeException("Formato de fecha inválido: " + fecha);
            }
        }
    }

    private Planta buscarPlanta(String nombre) {
        Planta planta = plantas.get(nombre);

        if (planta == null) {
            throw new RuntimeException("No existe la planta: " + nombre);
        }

        return planta;
    }

    private void registrarPlanta(Planta planta) {
        if (plantas.containsKey(planta.getNombre())) {
            throw new RuntimeException(
                "Ya existe una planta con el nombre: " + planta.getNombre()
            );
        }

        plantas.put(planta.getNombre(), planta);
    }

    private void validarMonitoreo(
            Planta planta,
            RegistroMonitoreo monitoreo,
            Class<?> tipoEsperado) {

        if (!tipoEsperado.isInstance(monitoreo)) {
            throw new RuntimeException(
                "El tipo de monitoreo no corresponde a la planta"
            );
        }

        planta.agregarMonitoreo(monitoreo);
    }

    // --------------------------------------------------
    // REGISTRO DE PLANTAS
    // --------------------------------------------------

    @Override
    public void registrarPlantaSolar(
            String nombre,
            String direccion,
            double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento,
            String fuentePrimaria,
            double eficienciaPromedio,
            double superficieCubierta,
            int cantidadPaneles,
            double horasRadiacionDiaria) {

        PlantaSolar planta = new PlantaSolar(
            nombre,
            direccion,
            capacidadMaxima,
            parsearFechaHora(fechaHoraPuestaEnFuncionamiento),
            fuentePrimaria,
            eficienciaPromedio,
            superficieCubierta,
            cantidadPaneles,
            horasRadiacionDiaria
        );

        registrarPlanta(planta);
    }

    @Override
    public void registrarPlantaEolica(
            String nombre,
            String direccion,
            double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento,
            String fuentePrimaria,
            double eficienciaPromedio,
            int cantidadAerogeneradores,
            double alturaTorres,
            double velocidadMinimaViento) {

        PlantaEolica planta = new PlantaEolica(
            nombre,
            direccion,
            capacidadMaxima,
            parsearFechaHora(fechaHoraPuestaEnFuncionamiento),
            fuentePrimaria,
            eficienciaPromedio,
            cantidadAerogeneradores,
            alturaTorres,
            velocidadMinimaViento
        );

        registrarPlanta(planta);
    }

    @Override
    public void registrarPlantaHidraulica(
            String nombre,
            String direccion,
            double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento,
            String fuentePrimaria,
            double eficienciaPromedio,
            int cantidadTurbinas,
            double capacidadEmbalse) {

        PlantaHidraulica planta = new PlantaHidraulica(
            nombre,
            direccion,
            capacidadMaxima,
            parsearFechaHora(fechaHoraPuestaEnFuncionamiento),
            fuentePrimaria,
            eficienciaPromedio,
            cantidadTurbinas,
            capacidadEmbalse
        );

        registrarPlanta(planta);
    }

    @Override
    public void registrarPlantaTermica(
            String nombre,
            String direccion,
            double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento,
            String tipoCombustible,
            double objetivoEmisionesCo2PorHora,
            double capacidadAlmacenamientoCombustible) {

        PlantaTermica planta = new PlantaTermica(
            nombre,
            direccion,
            capacidadMaxima,
            parsearFechaHora(fechaHoraPuestaEnFuncionamiento),
            tipoCombustible,
            objetivoEmisionesCo2PorHora,
            capacidadAlmacenamientoCombustible
        );

        registrarPlanta(planta);
    }

    // --------------------------------------------------
    // CONSULTAS Y ESTADO DE LAS PLANTAS
    // --------------------------------------------------

    @Override
    public List<String> consultarPlantas() {
        List<String> resultado = new ArrayList<>();

        for (Planta planta : plantas.values()) {
            resultado.add(
                planta.getNombre()
                + " (" + (planta.isActiva() ? "ACTIVA" : "INACTIVA") + "): "
                + planta.getFechaPuestaEnFuncionamiento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                
            );
        }

        return resultado;
    }

    @Override
    public double capacidadMinimaCuota(String nombre) {
        return buscarPlanta(nombre).capacidadMinimaCuota();
    }

    @Override
    public void activarPlanta(String nombre, String fechaHora) {
        buscarPlanta(nombre).activar(parsearFechaHora(fechaHora));
    }

    @Override
    public void desactivarPlanta(String nombre, String fechaHora) {
        buscarPlanta(nombre).desactivar(parsearFechaHora(fechaHora));
    }

    @Override
    public boolean estaActiva(String nombre) {
        return buscarPlanta(nombre).isActiva();
    }

    // --------------------------------------------------
    // REGISTRO DE MONITOREOS
    // --------------------------------------------------

    @Override
    public void registrarMonitoreo(
            String nombre,
            String fechaHora,
            double energiaGenerada,
            int incidencias) {

        Planta planta = buscarPlanta(nombre);

        RegistroMonitoreo monitoreo = new MonitoreoGeneral(
            parsearFechaHora(fechaHora),
            energiaGenerada,
            incidencias
        );

        if (!(planta instanceof PlantaSolar)
                && !(planta instanceof PlantaHidraulica)) {
            throw new RuntimeException(
                "Este monitoreo solo corresponde a plantas solares o hidráulicas"
            );
        }

        validarMonitoreo(planta, monitoreo, MonitoreoGeneral.class);
    }

    @Override
    public void registrarMonitoreo(
            String nombre,
            String fechaHora,
            double energiaGenerada,
            int incidencias,
            double velocidadViento) {

        Planta planta = buscarPlanta(nombre);

        if (!(planta instanceof PlantaEolica)) {
            throw new RuntimeException(
                "El monitoreo eólico requiere una planta eólica"
            );
        }

        RegistroMonitoreo monitoreo = new MonitoreoEolico(
            parsearFechaHora(fechaHora),
            energiaGenerada,
            incidencias,
            velocidadViento
        );

        validarMonitoreo(planta, monitoreo, MonitoreoEolico.class);
    }

    @Override
    public void registrarMonitoreo(
            String nombre,
            String fechaHora,
            double energiaGenerada,
            int incidencias,
            double co2Emitido,
            double combustibleUtilizado) {

        Planta planta = buscarPlanta(nombre);

        if (!(planta instanceof PlantaTermica)) {
            throw new RuntimeException(
                "El monitoreo térmico requiere una planta térmica"
            );
        }

        RegistroMonitoreo monitoreo = new MonitoreoTermico(
            parsearFechaHora(fechaHora),
            energiaGenerada,
            incidencias,
            co2Emitido,
            combustibleUtilizado
        );

        validarMonitoreo(planta, monitoreo, MonitoreoTermico.class);
    }

    // --------------------------------------------------
    // TOTALES E INCIDENCIAS
    // --------------------------------------------------

    @Override
    public double totalGenerado(String nombre) {
        return buscarPlanta(nombre).totalGenerado();
    }

    @Override
    public int[] incidenciasPorHora(String nombre) {
        Planta planta = buscarPlanta(nombre);
        int[] resultado = new int[24];

        for (RegistroMonitoreo monitoreo :
                planta.getMonitoreos().values()) {

            int hora = monitoreo.getFechaHora().getHour();
            resultado[hora] += monitoreo.getIncidencias();
        }

        return resultado;
    }

    // --------------------------------------------------
    // HISTOGRAMA DE CUMPLIMIENTO
    // --------------------------------------------------

    @Override
    public int[] histogramaCumplimiento(
            String nombre,
            String fechaDesde,
            String fechaHasta) {

        Planta planta = buscarPlanta(nombre);

        LocalDate desde = parsearFecha(fechaDesde);
        LocalDate hasta = parsearFecha(fechaHasta);

        if (hasta.isBefore(desde)) {
            throw new RuntimeException(
                "La fecha final no puede ser anterior a la fecha inicial"
            );
        }

        int[] histograma = new int[24];

        // Cada hora guarda los días distintos en los que se cumplió la cuota.
        Map<Integer, Set<LocalDate>> diasPorHora = new HashMap<>();

        for (int hora = 0; hora < 24; hora++) {
            diasPorHora.put(hora, new HashSet<>());
        }

        for (RegistroMonitoreo monitoreo :
                planta.getMonitoreos().values()) {

            LocalDateTime fecha = monitoreo.getFechaHora();
            LocalDate dia = fecha.toLocalDate();

            if (dia.isBefore(desde) || dia.isAfter(hasta)) {
                continue;
            }

            if (planta.cumpleCuota(monitoreo)) {
                diasPorHora.get(fecha.getHour()).add(dia);
            }
        }

        for (int hora = 0; hora < 24; hora++) {
            histograma[hora] = diasPorHora.get(hora).size();
        }

        return histograma;
    }

    // --------------------------------------------------
    // ESTADÍSTICAS DE PLANTAS RENOVABLES
    // --------------------------------------------------

    @Override
    public Map<String, Integer> plantasRenovablesPorFuente() {
        Map<String, Integer> resultado = new HashMap<>();

        for (Planta planta : plantas.values()) {
            if (planta instanceof PlantaRenovable) {
                PlantaRenovable renovable = (PlantaRenovable) planta;
                String fuente = renovable.getFuentePrimaria();

                resultado.put(
                    fuente,
                    resultado.getOrDefault(fuente, 0) + 1
                );
            }
        }

        return resultado;
    }

    @Override
    public List<String> plantasQueNuncaCumplieronCuota() {
        List<String> resultado = new ArrayList<>();

        for (Planta planta : plantas.values()) {
            boolean cumplio = false;

            for (RegistroMonitoreo monitoreo :
                    planta.getMonitoreos().values()) {

                if (planta.cumpleCuota(monitoreo)) {
                    cumplio = true;
                    break;
                }
            }

            if (!cumplio) {
                resultado.add(planta.getNombre());
            }
        }

        return resultado;
    }

    // --------------------------------------------------
    // PLANTAS EÓLICAS CON HORAS SIN VIENTO SUFICIENTE
    // --------------------------------------------------

    @Override
    public String horasSinVelocidadMinimaPorPlantaEolica() {
        StringBuilder resultado = new StringBuilder();
        resultado.append("Plantas Eolicas: Horas con poco viento");

        for (Planta planta : plantas.values()) {
            if (!(planta instanceof PlantaEolica)) {
                continue;
            }

            PlantaEolica eolica = (PlantaEolica) planta;
            Map<Integer, Boolean> alcanzoVelocidad = new HashMap<>();
            Map<Integer, Boolean> tuvoRegistros = new HashMap<>();

            for (RegistroMonitoreo monitoreo :
                    eolica.getMonitoreos().values()) {

                MonitoreoEolico eolico = (MonitoreoEolico) monitoreo;
                int hora = eolico.getFechaHora().getHour();

                tuvoRegistros.put(hora, true);

                if (eolico.getVelocidadViento()
                        >= eolica.getVelocidadMinimaViento()) {
                    alcanzoVelocidad.put(hora, true);
                }
            }

            List<Integer> horasInsuficientes = new ArrayList<>();

            for (int hora = 0; hora < 24; hora++) {
                if (Boolean.TRUE.equals(tuvoRegistros.get(hora))
                        && !Boolean.TRUE.equals(alcanzoVelocidad.get(hora))) {
                    horasInsuficientes.add(hora);
                }
            }

            if (!horasInsuficientes.isEmpty()) {
                resultado.append("\n")
                    .append(eolica.getNombre())
                    .append(" (velocidad mínima: ")
                    .append(eolica.getVelocidadMinimaViento())
                    .append("): ");

                for (int i = 0; i < horasInsuficientes.size(); i++) {
                    if (i > 0) {
                        resultado.append(", ");
                    }

                    resultado.append(String.format("%02d",horasInsuficientes.get(i)));
                }
            }
        }

        return resultado.toString();
    }

    // --------------------------------------------------
    // LIMPIEZA DE MONITOREOS
    // --------------------------------------------------

    @Override
    public int limpiarMonitoreosSinDatos() {
        int eliminados = 0;

        for (Planta planta : plantas.values()) {
            Map<LocalDateTime, RegistroMonitoreo> monitoreos =
                planta.getMonitoreos();

            List<LocalDateTime> paraEliminar = new ArrayList<>();

            for (Map.Entry<LocalDateTime, RegistroMonitoreo> entrada
                    : monitoreos.entrySet()) {

                RegistroMonitoreo monitoreo = entrada.getValue();

                if (monitoreo.getEnergiaGenerada() == 0
                        && monitoreo.getIncidencias() == 0) {
                    paraEliminar.add(entrada.getKey());
                }
            }

            for (LocalDateTime fecha : paraEliminar) {
                monitoreos.remove(fecha);
                eliminados++;
            }
        }

        return eliminados;
    }

    // --------------------------------------------------
    // ORDENAMIENTO POR ENERGÍA
    // --------------------------------------------------

    @Override
    public List<String> plantasOrdenadasPorEnergiaGenerada() {
        List<Planta> ordenadas = new ArrayList<>(plantas.values());

        ordenadas.sort(
            Comparator.comparingDouble(Planta::totalGenerado).reversed()
        );

        List<String> resultado = new ArrayList<>();

        for (Planta planta : ordenadas) {
            resultado.add(
                planta.getNombre() + ": " + planta.totalGenerado()
            );
        }

        return resultado;
    }

    // --------------------------------------------------
    // CONSULTA DE PLANTAS INACTIVAS EN UNA FECHA
    // --------------------------------------------------

    @Override
    public List<String> plantasInactivasEn(String fechaHora) {
        LocalDateTime fecha = parsearFechaHora(fechaHora);
        List<String> resultado = new ArrayList<>();

        for (Planta planta : plantas.values()) {
            if (planta.estabaInactivaEn(fecha)) {
                resultado.add(planta.getNombre());
            }
        }

        return resultado;
    }

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder("SmartGrids");

        for (Planta planta : plantas.values()) {
            resultado.append("\n").append(planta);
        }

        return resultado.toString();
    }
}
