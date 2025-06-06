package org.example.academycorsi.repository;

import org.example.academycorsi.data.dto.DiscenteDTO;
import org.example.academycorsi.data.entity.CorsoDiscenti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorsoDiscentiRepository extends JpaRepository<CorsoDiscenti, Long> {
    @Query("SELECT cd.discenteId FROM CorsoDiscenti cd WHERE cd.corsoId = :idCorso")
    List<Long> findIdsDiscenteByIdCorso(@Param("idCorso") Long idCorso);


    void deleteByCorsoId(Long corsoId);

}
