package itson.dominiorummy.mappers;

import itson.dominiorummy.entidades.Ficha;
import itson.dominiorummy.entidades.Tablero;
import itson.rummydtos.FichaDTO;
import itson.rummydtos.TableroDTO;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author victoria
 */
public class FichaMapper {

    public static List<Ficha> toEntidad(List<FichaDTO> dtos) {
        return dtos.stream().map(dto -> FichaMapper.toEntidad(dto)).collect(Collectors.toList());
    }

    public static Ficha toEntidad(FichaDTO dto) {
        return new Ficha(dto.getId(), dto.getNumero(), dto.getColor(), dto.isEsComodin());
    }

    public static TableroDTO toDTO(Tablero tablero) {
        return new TableroDTO();
    }

    public static FichaDTO toDTO(Ficha ficha) {
        return new FichaDTO(ficha.getId(), ficha.getNumero(), ficha.getColor(), ficha.isEsComodin());
    }

    public static List<FichaDTO> toDTO(List<Ficha> fichas) {
        return fichas.stream().map(ficha -> FichaMapper.toDTO(ficha)).collect(Collectors.toList());
    }
}
