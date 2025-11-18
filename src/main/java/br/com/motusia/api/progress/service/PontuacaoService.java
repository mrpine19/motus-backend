package br.com.motusia.api.progress.service;

import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.learning.model.Desafio;
import br.com.motusia.api.progress.dto.FeedbackDesafioDto;
import br.com.motusia.api.progress.dto.RespostaDesafioDto;
import br.com.motusia.api.progress.model.Pontuacao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@ApplicationScoped
public class PontuacaoService {

    private static final Logger logger = LoggerFactory.getLogger(PontuacaoService.class);
    private static final int PONTOS_BASE_ACERTO = 10;
    private static final int PONTOS_POR_ESFORCO = 5;
    private static final int GATILHO_REAVALIACAO = 5;

    @Inject
    Event<Aluno> reavaliacaoEvent;

    @Inject
    PontuacaoPersistenceService persistenceService; // Injeta o novo Bean

    // IMPORTANTE: Removemos @Transactional daqui
    public FeedbackDesafioDto submeterResposta(Long alunoId, RespostaDesafioDto respostaDto) {

        // 1. Validação e Busca de Dados (Leituras são seguras)
        Desafio desafio = Desafio.findById(respostaDto.getDesafioId());
        if (desafio == null) {
            throw new NotFoundException("Desafio não encontrado");
        }
        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            throw new NotFoundException("Aluno não encontrado");
        }
        int streakAtual = aluno.getStreakAtual();

        // 2. Processamento e Cálculo (em memória)
        boolean acertou = desafio.getRespostaCorreta().trim().equalsIgnoreCase(respostaDto.getRespostaSubmetida().trim());
        int pontosGanhos = acertou ? PONTOS_BASE_ACERTO : PONTOS_POR_ESFORCO;
        int novaStreak = acertou ? streakAtual + 1 : 0;

        // 3. Delega a persistência da Pontuação (Transação Curta 1)
        persistenceService.persistirPontuacao(aluno.getId(), desafio.getId(), acertou, pontosGanhos, respostaDto.getTempoGastoSegundos());

        // 4. Delega a atualização do Aluno e o Gatilho (Transação Curta 2)
        // Esta transação agora LÊ os dados que a Transação 1 ACABOU de commitar.
        long totalConcluidos = persistenceService.atualizarPerfilEVerificarGatilho(aluno.getId(), novaStreak);

        // 5. Dispara o evento (Após as transações de DB)
        if (totalConcluidos > 0 && totalConcluidos % GATILHO_REAVALIACAO == 0) {
            logger.info("Disparando evento de reavaliação para o aluno {} com {} desafios concluídos.", aluno.getId(), totalConcluidos);
            reavaliacaoEvent.fire(aluno); // Dispara o evento para o TrilhaService
        }

        // 6. Retorno do Feedback
        String feedbackMsg = acertou ? desafio.getFeedbackExplicacao() : "Que pena, você errou. Mas ganhou " + PONTOS_POR_ESFORCO + " pontos pelo esforço! " + desafio.getFeedbackExplicacao();
        return new FeedbackDesafioDto(acertou, feedbackMsg, pontosGanhos, novaStreak, totalConcluidos);
    }
}