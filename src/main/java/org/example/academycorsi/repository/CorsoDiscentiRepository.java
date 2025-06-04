package org.example.academycorsi.repository;

import org.example.academycorsi.data.dto.DiscenteDTO;
import org.example.academycorsi.data.entity.CorsoDiscenti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorsoDiscentiRepository extends JpaRepository<CorsoDiscenti, Long> {
    List<CorsoDiscenti> findByCorsoId(Long corsoId);
    void deleteByCorsoId(Long corsoId);

}
