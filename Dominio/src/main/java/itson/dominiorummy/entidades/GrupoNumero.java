package itson.dominiorummy.entidades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GrupoNumero extends Grupo {

    public GrupoNumero(String id, List<FichaPlaced> fichas) {
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

        Integer numRef = null;
        Set<String> coloresVistos = new HashSet<>();
        int comodines = 0;

        for (Ficha f : base) {
            if (f.isEsComodin()) {
                comodines++;
                continue;
            }

            if (numRef == null) {
                numRef = f.getNumero();
            } else if (f.getNumero() != numRef) {
                return false;
            }
            if (!coloresVistos.add(f.getColor())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int calcularPuntos() {
        if (fichas.isEmpty()) {
            return 0;
        }

        int valorReferencia = 0;
        for (FichaPlaced fp : fichas) {
            if (!fp.getFicha().isEsComodin()) {
                valorReferencia = fp.getFicha().getNumero();
                break;
            }
        }

        return valorReferencia * fichas.size();
    }

    @Override
    public Grupo clonar() {
        List<FichaPlaced> copiaFichas = new ArrayList<>();

        for (FichaPlaced fp : this.fichas) {
            copiaFichas.add(fp.clonar());
        }

        return new GrupoNumero(this.id, copiaFichas);
    }
}
