package itson.dominiorummy.entidades;

public class FichaPlaced {

    private final Ficha ficha;
    private final String placedBy;
    private final int placedInTurn;

    public FichaPlaced(Ficha ficha, String placedBy, int placedInTurn) {
        this.ficha = ficha;
        this.placedBy = placedBy;
        this.placedInTurn = placedInTurn;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public String getPlacedBy() {
        return placedBy;
    }

    public int getPlacedInTurn() {
        return placedInTurn;
    }

    /** Clonado profundo (para rollback) */
    public FichaPlaced clonar() {
        return new FichaPlaced(ficha.clonar(), placedBy, placedInTurn);
    }
    
    @Override
    public String toString() {
        Ficha f = getFicha();
        return String.format("[%s %d (%s) placedBy=%s turno=%d]",
                f.getColor(), f.getNumero(), f.getId(), placedBy, placedInTurn);
    }

}
