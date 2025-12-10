/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.dominiorummy.mappers;

import itson.dominiorummy.entidades.Ficha;
import itson.dominiorummy.entidades.FichaPlaced;
import itson.dominiorummy.entidades.Grupo;
import itson.dominiorummy.entidades.Tablero;
import itson.rummydtos.FichaDTO;
import itson.rummydtos.GrupoDTO;
import itson.rummydtos.TableroDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dana Chavez
 */
public class TableroMapper {

    public static TableroDTO toDTO(Tablero tablero) {
        List<GrupoDTO> gruposDTO = new ArrayList<>();
        for (Grupo grupo : tablero.getGruposEnMesa()) {
            if (grupo == null) {
                continue;
            }
            GrupoDTO dto = grupoaDTO(grupo);
            if (dto == null) {
                continue;
            }
            gruposDTO.add(dto);
        }
        return new TableroDTO(gruposDTO);
    }

    private static GrupoDTO grupoaDTO(Grupo grupo) {
        if (grupo == null) {
            return null;
        }
        if (grupo.getFichas() == null) {
            return null;
        }
        List<Ficha> fichas = grupo.getFichas().stream()
                .filter(fp -> fp != null && fp.getFicha() != null)
                .map(FichaPlaced::getFicha)
                .toList();
        if (fichas.isEmpty()) {
            return null;
        }
        List<FichaDTO> fichasDTO = FichaMapper.toDTO(fichas);
        String id = grupo.getId();
        if (id == null) {
            return null;
        }
        return new GrupoDTO(id, fichasDTO, true);
    }
}
