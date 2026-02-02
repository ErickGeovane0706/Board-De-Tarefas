package com.desafioDIO.Board_de_Tarefas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*; // Importa todas as anotações do JPA
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Usando List para garantir a ordem das colunas exigida no desafio
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("columnOrder ASC") // Garante que venha ordenado do banco
    @JsonManagedReference // <--- Adicione isto aqui
    private List<BoardColumn> boardColumns = new ArrayList<>();

}