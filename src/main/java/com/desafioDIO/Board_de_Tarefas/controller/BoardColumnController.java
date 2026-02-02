package com.desafioDIO.Board_de_Tarefas.controller;

import com.desafioDIO.Board_de_Tarefas.model.BoardColumn;
import com.desafioDIO.Board_de_Tarefas.service.BoardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/columns")
public class BoardColumnController {

    @Autowired
    private BoardColumnService boardColumnService;

    @PostMapping
    public ResponseEntity<BoardColumn> create(@RequestBody BoardColumn column) {
        // Usa o método de usuário que restringe a PENDING
        return ResponseEntity.status(HttpStatus.CREATED).body(boardColumnService.createUserColumn(column));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boardColumnService.delete(id);
        return ResponseEntity.noContent().build();
    }
}