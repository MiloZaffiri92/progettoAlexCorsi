package org.example.academycorsi.converter;

import org.example.academycorsi.data.dto.CorsoDTO;
import org.example.academycorsi.data.entity.Corso;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CorsoMapper {

    CorsoDTO corsoToDto(Corso corso);


    Corso corsoToEntity(CorsoDTO corsoDTO);
}
