package com.desafioDIO.Board_de_Tarefas.service;
import com.desafioDIO.Board_de_Tarefas.exception.BusinessException;
import com.desafioDIO.Board_de_Tarefas.exception.ResourceNotFoundException;
import com.desafioDIO.Board_de_Tarefas.model.BoardColumn;
import com.desafioDIO.Board_de_Tarefas.model.Card;
import com.desafioDIO.Board_de_Tarefas.model.enums.BoardColumnKind;
import com.desafioDIO.Board_de_Tarefas.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private BoardColumnService boardColumnService;

    public Card Create(Card card) {
        // 1. Buscamos a coluna completa no banco usando o ID enviado no JSON
        BoardColumn column = boardColumnService.findById(card.getBoardColumn().getId());

        // 2. Agora validamos usando o 'kind' que veio do banco de dados
        if (column.getKind() != BoardColumnKind.INITIAL) {
            throw new BusinessException("Um novo card deve obrigatoriamente começar na coluna INITIAL");
        }

        // 3. Vinculamos a coluna carregada ao card e salvamos
        card.setBoardColumn(column);
        return cardRepository.save(card);
    }
    public Card findById(Long idCard) {
        // Se não achar o card, lança a exceção que o nosso GlobalHandler já sabe tratar
        return cardRepository.findById(idCard)
                .orElseThrow(() -> new ResourceNotFoundException("Card com ID " + idCard + " não encontrado."));
    }
    public void delete(Long idCard) {
        // Verificamos se existe antes de tentar deletar
        Card card = findById(idCard);
        cardRepository.delete(card);
    }

    public List<Card> FindAll() {
        return cardRepository.findAll();
    }
    public Card update(Long id, Card cardAtualizado) {
        Card cardExistente = findById(id);

        // 1. Atualiza campos básicos
        cardExistente.setTitle(cardAtualizado.getTitle());
        cardExistente.setDescription(cardAtualizado.getDescription());

        // 2. Lógica de Bloqueio
        // Se o usuário está tentando bloquear agora, o motivo é obrigatório
        if (cardAtualizado.isBlocked() && (cardAtualizado.getBlockedReason() == null || cardAtualizado.getBlockedReason().isBlank())) {
            throw new BusinessException("Para bloquear um card, é obrigatório informar o motivo.");
        }

        // Se o card estava bloqueado e agora está sendo desbloqueado, limpamos o motivo
        if (!cardAtualizado.isBlocked()) {
            cardExistente.setBlockedReason(null);
        } else {
            cardExistente.setBlockedReason(cardAtualizado.getBlockedReason());
        }

        cardExistente.setBlocked(cardAtualizado.isBlocked());

        return cardRepository.save(cardExistente);
    }



    public Card moverCard(Long idCard, Long idNovaColuna) {
        // 1. Busca o card e a nova coluna (Garantindo que existem)
        Card card = findById(idCard);
        BoardColumn novaColuna = boardColumnService.findById(idNovaColuna);

        // 2. Regra de Bloqueio: Se estiver bloqueado, não move
        if (card.isBlocked()) {
            throw new BusinessException("O card está bloqueado e não pode ser movido. Motivo: " + card.getBlockedReason());
        }

        // 3. Pegamos a ordem atual e a nova ordem para comparar
        int ordemAtual = card.getBoardColumn().getColumnOrder();
        int ordemNova = novaColuna.getColumnOrder();

        // 4. Regra de Sequência: Só pode mover para a PRÓXIMA (ou para CANCEL)
        boolean ehProximaEtapa = (ordemNova == ordemAtual + 1);
        boolean ehCancelamento = (novaColuna.getKind() == BoardColumnKind.CANCEL);

        if (!ehProximaEtapa && !ehCancelamento) {
            throw new BusinessException("Movimentação inválida! Você só pode mover o card para a próxima coluna ou para o cancelamento.");
        }

        // 5. Se passou pelas validações, atualiza e salva
        card.setBoardColumn(novaColuna);
        return cardRepository.save(card);
    }
}
