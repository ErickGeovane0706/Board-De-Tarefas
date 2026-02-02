package com.desafioDIO.Board_de_Tarefas.controller;

import com.desafioDIO.Board_de_Tarefas.model.Card;
import com.desafioDIO.Board_de_Tarefas.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<Card> create(@RequestBody Card card) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.Create(card));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> update(@PathVariable Long id, @RequestBody Card card) {
        return ResponseEntity.ok(cardService.update(id, card));
    }

    @PatchMapping("/{id}/mover")
    public ResponseEntity<Card> mover(@PathVariable Long id, @RequestParam Long novaColunaId) {
        return ResponseEntity.ok(cardService.moverCard(id, novaColunaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}