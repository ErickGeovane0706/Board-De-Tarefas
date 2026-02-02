package com.desafioDIO.Board_de_Tarefas.service;

import com.desafioDIO.Board_de_Tarefas.exception.BusinessException;
import com.desafioDIO.Board_de_Tarefas.exception.ResourceNotFoundException;
import com.desafioDIO.Board_de_Tarefas.model.BoardColumn;
import com.desafioDIO.Board_de_Tarefas.model.enums.BoardColumnKind;
import com.desafioDIO.Board_de_Tarefas.repository.BoardColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BoardColumnService {

    @Autowired
    private BoardColumnRepository repository;

    public BoardColumn findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coluna não encontrada com o ID: " + id));
    }

    public List<BoardColumn> findAllByBoardId(Long boardId) {
        return repository.findByBoardIdOrderByColumnOrderAsc(boardId);
    }

    // CREATE SISTEMA (Usado para o 'Setup' inicial do Board)
    public BoardColumn saveSystemColumn(BoardColumn column) {
        // Se a ordem não foi definida no BoardService, definimos como a última
        if (column.getColumnOrder() == null) {
            Integer maiorOrdem = repository.findMaxOrderByBoardId(column.getBoard().getId());
            column.setColumnOrder(maiorOrdem == null ? 0 : maiorOrdem + 1);
        }

        BoardColumn salva = repository.save(column);
        reordenarColunas(salva.getBoard().getId());
        return salva;
    }

    // CREATE USUÁRIO (Restrito a PENDING)
    public BoardColumn createUserColumn(BoardColumn novaColuna) {
        if (novaColuna.getKind() != BoardColumnKind.PENDING) {
            throw new BusinessException("O usuário só pode criar colunas do tipo PENDING.");
        }

        // Proteção: Garante que o Board não é nulo antes de tentar pegar o ID
        if (novaColuna.getBoard() == null || novaColuna.getBoard().getId() == null) {
            throw new BusinessException("O ID do Board é obrigatório.");
        }

        novaColuna.setColumnOrder(1);

        // Salva a coluna
        BoardColumn salva = repository.save(novaColuna);

        // Agora o board_id não será nulo e a reordenação terá um ID válido para filtrar
        reordenarColunas(salva.getBoard().getId());

        return salva;
    }

    public void delete(Long id) {
        BoardColumn column = findById(id);
        Long boardId = column.getBoard().getId(); // Pegamos o ID antes de deletar

        if (column.getKind() != BoardColumnKind.PENDING) {
            throw new BusinessException("Apenas colunas PENDING podem ser removidas pelo usuário.");
        }

        long totalPending = repository.countByBoardIdAndKind(boardId, BoardColumnKind.PENDING);
        if (totalPending <= 1) {
            throw new BusinessException("O Board deve ter pelo menos uma coluna PENDING.");
        }

        repository.delete(column);
        reordenarColunas(boardId); // Fecha o buraco na sequência
    }

    private void reordenarColunas(Long boardId) {
        List<BoardColumn> colunas = repository.findByBoardIdOrderByColumnOrderAsc(boardId);
        for (int i = 0; i < colunas.size(); i++) {
            BoardColumn col = colunas.get(i);
            if (col.getColumnOrder() != i) {
                col.setColumnOrder(i);
                repository.save(col);
            }
        }
    }
}