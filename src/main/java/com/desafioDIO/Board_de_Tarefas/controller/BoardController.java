package com.desafioDIO.Board_de_Tarefas.controller;

import com.desafioDIO.Board_de_Tarefas.model.Board;
import com.desafioDIO.Board_de_Tarefas.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping
    public ResponseEntity<Board> create(@RequestBody Board board) {
        // Ao criar o Board, o Service já cria as colunas automáticas
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.create(board));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> findById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Board> update(@PathVariable Long id, @RequestBody Board board) {
        return ResponseEntity.ok(boardService.update(id, board));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
