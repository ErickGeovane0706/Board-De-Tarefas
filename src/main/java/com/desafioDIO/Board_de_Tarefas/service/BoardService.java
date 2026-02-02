package com.desafioDIO.Board_de_Tarefas.service;

import com.desafioDIO.Board_de_Tarefas.exception.ResourceNotFoundException;
import com.desafioDIO.Board_de_Tarefas.model.Board;
import com.desafioDIO.Board_de_Tarefas.model.BoardColumn;
import com.desafioDIO.Board_de_Tarefas.model.enums.BoardColumnKind;
import com.desafioDIO.Board_de_Tarefas.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardColumnService boardColumnService;

    public Board create(Board board) {
        // 1. Salva o Board primeiro para gerar o ID
        Board boardSalvo = boardRepository.save(board);

        // 2. Cria o setup inicial de colunas usando o serviço de colunas
        // Note que usamos o saveSystemColumn para pular as travas de usuário
        criarColunaPadrao(boardSalvo, "A Fazer", BoardColumnKind.INITIAL);
        criarColunaPadrao(boardSalvo, "Em Andamento", BoardColumnKind.PENDING);
        criarColunaPadrao(boardSalvo, "Concluído", BoardColumnKind.FINAL);
        criarColunaPadrao(boardSalvo, "Cancelado", BoardColumnKind.CANCEL);

        return boardSalvo;
    }

    private void criarColunaPadrao(Board board, String nome, BoardColumnKind tipo) {
        BoardColumn col = new BoardColumn();
        col.setName(nome); // Define o nome que você passou (ex: "A Fazer")
        col.setKind(tipo);
        col.setBoard(board);
        // O columnOrder será definido automaticamente pelo seu saveSystemColumn
        boardColumnService.saveSystemColumn(col);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board não encontrado com o ID: " + id));
    }
    public Board update(Long id, Board boardAtualizado) {
        // 1. Busca o board existente ou lança 404
        Board boardExistente = findById(id);

        // 2. Atualiza apenas os campos permitidos (neste caso, o nome)
        boardExistente.setName(boardAtualizado.getName());

        // 3. Salva e retorna
        return boardRepository.save(boardExistente);
    }
    public void delete(Long id) {
        // 1. Verifica se o Board existe (se não existir, o findById já lança 404)
        Board board = findById(id);

        // 2. Executa a deleção
        // Graças ao CascadeType.ALL nas entidades, colunas e cards serão removidos automaticamente
        boardRepository.delete(board);
    }
}
