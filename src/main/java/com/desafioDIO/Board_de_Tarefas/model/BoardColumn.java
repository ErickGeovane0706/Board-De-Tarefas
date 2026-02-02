package com.desafioDIO.Board_de_Tarefas.model;

import com.desafioDIO.Board_de_Tarefas.model.enums.BoardColumnKind;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class BoardColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Novo campo para o nome amigável da coluna
    @Column(nullable = false)
    private String name;

    @Column(name = "column_order")
    private Integer columnOrder;

    @Enumerated(EnumType.STRING)
    private BoardColumnKind kind;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board board;
    @OneToMany(mappedBy = "boardColumn", cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    @JsonManagedReference
    private List<Card> cards = new ArrayList<>();
}