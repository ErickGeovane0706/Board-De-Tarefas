package com.desafioDIO.Board_de_Tarefas.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
@Data
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now(); // Já inicializa com a data atual

    private boolean blocked;
    private String blockedReason;

    @ManyToOne
    @JoinColumn(name = "column_id")
    @JsonBackReference
    private BoardColumn boardColumn;
}
