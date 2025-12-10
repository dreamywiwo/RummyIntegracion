package itson.dominiorummy.entidades;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GrupoSecuencia extends Grupo {

    public GrupoSecuencia(String id, List<FichaPlaced> fichas) {
        super(id, fichas);
    }

    @Override
    public boolean validarReglas() {
        if (fichas.isEmpty()) {
            return false;
        }
        if (fichas.size() == 1) {
            return true;
        }
        List<Ficha> base = fichas.stream().map(FichaPlaced::getFicha).collect(Collectors.toList());
        List<Ficha> normales = base.stream().filter(f -> !f.isEsComodin()).collect(Collectors.toList());

        if (normales.isEmpty()) {
            return true;
        }
        // 1. Validar Color Único
        String colorRef = normales.get(0).getColor();
        for (Ficha f : normales) {
            if (!f.getColor().equals(colorRef)) {
                return false;
            }
        }

        // 2. Validar Secuencia Numérica
        normales.sort(Comparator.comparingInt(Ficha::getNumero));

        int comodinesDisponibles = base.size() - normales.size();
        int numeroEsperado = normales.get(0).getNumero();

        for (Ficha f : normales) {
            if (f.getNumero() < numeroEsperado) {
                return false;
            }

            // Si hay hueco
            while (f.getNumero() > numeroEsperado) {
                if (comodinesDisponibles > 0) {
                    comodinesDisponibles--;
                    numeroEsperado++;
                } else {
                    return false; // ERROR: Hueco sin comodín
                }
            }
            numeroEsperado++;
        }

        return true;
    }

    @Override
    public int calcularPuntos() {
        if (fichas.isEmpty()) {
            return 0;
        }

        int primerNumeroNormal = -1;
        int indicePrimerNormal = -1;

        for (int i = 0; i < fichas.size(); i++) {
            if (!fichas.get(i).getFicha().isEsComodin()) {
                primerNumeroNormal = fichas.get(i).getFicha().getNumero();
                indicePrimerNormal = i;
                break;
            }
        }

        if (primerNumeroNormal == -1) {
            return 0;
        }

        int numeroInicio = primerNumeroNormal - indicePrimerNormal;

        int sumaTotal = 0;
        for (int i = 0; i < fichas.size(); i++) {
            sumaTotal += (numeroInicio + i);
        }

        return sumaTotal;
    }

    @Override
    public Grupo clonar() {
        List<FichaPlaced> copiaFichas = new ArrayList<>();

        for (FichaPlaced fp : this.fichas) {
            copiaFichas.add(fp.clonar());
        }

        return new GrupoSecuencia(this.id, copiaFichas);
    }
}
