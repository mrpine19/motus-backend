package br.com.motusia.api.progress.service;

import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.learning.model.Desafio;
import br.com.motusia.api.progress.model.Pontuacao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.Date;

@ApplicationScoped
public class PontuacaoPersistenceService {

    /**
     * TRANSAÇÃO 1: Focada apenas em persistir o log de pontuação.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW) // Força uma nova transação
    public void persistirPontuacao(Long alunoId, Long desafioId, boolean acertou, int pontosGanhos, Double tempoGasto) {

        // Busca as referências DENTRO da transação
        Aluno alunoRef = Aluno.findById(alunoId);
        Desafio desafioRef = Desafio.findById(desafioId);

        if (alunoRef == null || desafioRef == null) {
            throw new NotFoundException("Falha na persistência: Aluno ou Desafio não encontrado.");
        }

        Pontuacao pontuacao = new Pontuacao();
        pontuacao.setAluno(alunoRef);
        pontuacao.setDesafio(desafioRef);
        pontuacao.setAcertou(acertou);
        pontuacao.setPontos(pontosGanhos);
        pontuacao.setTempoGastoSegundos(tempoGasto);
        pontuacao.setDataConclusao(new Date());
        pontuacao.persist();
    } // Transação 1 commita aqui.

    /**
     * TRANSAÇÃO 2: Atualiza o aluno e LÊ o total de pontuações.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW) // Força outra nova transação
    public long atualizarPerfilEVerificarGatilho(Long alunoId, int novaStreak) {

        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            throw new NotFoundException("Falha ao atualizar perfil: Aluno não encontrado.");
        }

        // Atualiza o aluno (UPDATE)
        aluno.setStreakAtual(novaStreak);
        aluno.persist(); // (Opcional para updates em Panache, mas garante a escrita)

        // LEITURA SEGURA: O count() agora lê os dados commitados pela Transação 1.
        long totalConcluidos = Pontuacao.count("aluno", aluno);

        return totalConcluidos;
    } // Transação 2 commita aqui.
}