package com.desafioDIO.Board_de_Tarefas.repository;

import com.desafioDIO.Board_de_Tarefas.model.BoardColumn;
import com.desafioDIO.Board_de_Tarefas.model.enums.BoardColumnKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findByBoardIdOrderByColumnOrderAsc(Long boardId);

    long countByBoardIdAndKind(Long boardId, BoardColumnKind kind);

    @Query("SELECT MAX(bc.columnOrder) FROM BoardColumn bc WHERE bc.board.id = :boardId")
    Integer findMaxOrderByBoardId(@Param("boardId") Long boardId);
}
