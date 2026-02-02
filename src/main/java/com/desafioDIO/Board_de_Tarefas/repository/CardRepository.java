package com.desafioDIO.Board_de_Tarefas.repository;

import com.desafioDIO.Board_de_Tarefas.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
