package org.example.academycorsi.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "corsi_discente")
public class CorsoDiscenti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "corso_id")
    private Long corsoId;

    @Column(name = "discente_id")
    private Long discenteId;
}
