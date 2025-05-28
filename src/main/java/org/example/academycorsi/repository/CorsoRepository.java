package org.example.academycorsi.repository;

import org.example.academycorsi.data.entity.Corso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorsoRepository extends JpaRepository<Corso, Long> {
}
