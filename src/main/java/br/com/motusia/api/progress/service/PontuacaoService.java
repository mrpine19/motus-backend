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

import java.util.Date;

@ApplicationScoped
public class PontuacaoService {

    private static final int PONTOS_BASE_ACERTO = 10;
    private static final int PONTOS_POR_ESFORCO = 5;
    private static final int GATILHO_REAVALIACAO = 5;

    @Inject
    Event<Aluno> reavaliacaoEvent;


    @Transactional
    public FeedbackDesafioDto submeterResposta(Long alunoId, RespostaDesafioDto respostaDto) {

        // 1. Validação e Busca de Dados
        Desafio desafio = Desafio.findById(respostaDto.getDesafioId());
        if (desafio == null) {
            throw new RuntimeException("Desafio não encontrado");
        }
        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            throw new RuntimeException("Aluno não encontrado");
        }

        // 2. Processamento e Cálculo
        boolean acertou = desafio.getRespostaCorreta().trim().equalsIgnoreCase(respostaDto.getRespostaSubmetida().trim());
        int pontosGanhos;
        int novaStreak;

        if (acertou) {
            pontosGanhos = PONTOS_BASE_ACERTO;
            novaStreak = aluno.getStreakAtual() + 1;
        } else {
            pontosGanhos = PONTOS_POR_ESFORCO; // Ponto pelo esforço
            novaStreak = 0; // Reset
        }

        // 3. Persistência do Log (Pontuacao)
        Pontuacao pontuacao = new Pontuacao();
        pontuacao.setAluno(aluno);
        pontuacao.setDesafio(desafio);
        pontuacao.setAcertou(acertou);
        pontuacao.setPontos(pontosGanhos);
        pontuacao.setTempoGastoSegundos(respostaDto.getTempoGastoSegundos());
        pontuacao.setDataConclusao(new Date());
        pontuacao.persist();

        // 4. ATUALIZAÇÃO DO PERFIL DO ALUNO (RF5)
        aluno.setStreakAtual(novaStreak);
        aluno.persist();

        // 5. GATILHO DE REAVALIAÇÃO (US 4)
        long totalConcluidos = Pontuacao.count("aluno", aluno);
        if (totalConcluidos > 0 && totalConcluidos % GATILHO_REAVALIACAO == 0) {
            reavaliacaoEvent.fire(aluno);
        }

        // 6. RETORNO DO FEEDBACK (RF6)
        String feedbackMsg = acertou ? desafio.getFeedbackExplicacao() : "Que pena, você errou. Mas ganhou 5 pontos pelo esforço! " + desafio.getFeedbackExplicacao();

        return new FeedbackDesafioDto(
                acertou,
                feedbackMsg,
                pontosGanhos,
                novaStreak,
                totalConcluidos
        );
    }
}