package itson.rummypresentacion.utils;

import itson.rummypresentacion.utils.ContenedorFichas;
import itson.rummydtos.FichaDTO;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.List;

public class FichaTransferable implements Transferable {

    public static final DataFlavor FICHA_FLAVOR = new DataFlavor(FichaTransferable.class, "Fichas Rummy");
    
    private List<FichaDTO> fichas; 
    private ContenedorFichas origen;

    public FichaTransferable(FichaDTO ficha, ContenedorFichas origen) {
        this.fichas = new ArrayList<>();
        this.fichas.add(ficha);
        this.origen = origen;
    }

    public FichaTransferable(List<FichaDTO> fichas, ContenedorFichas origen) {
        this.fichas = fichas;
        this.origen = origen;
    }

    public List<FichaDTO> getFichas() {
        return fichas;
    }
    
    public FichaDTO getFichaPrincipal() {
        return fichas.isEmpty() ? null : fichas.get(0);
    }

    public ContenedorFichas getOrigen() {
        return origen;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{FICHA_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(FICHA_FLAVOR);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return this;
    }
}