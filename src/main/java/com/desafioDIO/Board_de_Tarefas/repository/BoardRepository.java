package com.desafioDIO.Board_de_Tarefas.repository;

import com.desafioDIO.Board_de_Tarefas.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
