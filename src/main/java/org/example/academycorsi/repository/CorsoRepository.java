package org.example.academycorsi.repository;

import org.example.academycorsi.data.entity.Corso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CorsoRepository extends JpaRepository<Corso, Long> {

    @Query("SELECT c.id FROM Corso c WHERE c.nome = :nome")
    Long findIdByNome(@Param("nome") String nome);

}