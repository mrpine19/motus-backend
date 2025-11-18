package br.com.motusia.api.progress.service;

import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.learning.model.NivelCompetencia;
import br.com.motusia.api.progress.model.HistoricoNivel;
import br.com.motusia.api.progress.model.Pontuacao;
import io.quarkus.panache.common.Sort;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@ApplicationScoped
public class ReavaliacaoService {

    private static final Logger logger = LoggerFactory.getLogger(ReavaliacaoService.class);
    private static final int DESAFIOS_PARA_AVALIACAO = 10;

    @Inject
    TrilhaService trilhaService;

    @Blocking
    public void onReavaliacaoRequerida(@ObservesAsync Aluno aluno) {
        logger.info("Iniciando reavaliação assíncrona para o aluno ID: {}", aluno.getId());
        processarReavaliacao(aluno);
    }

    @Retry(maxRetries = 3, delay = 2000) // RNF1: Mecanismo de Retry
    @Transactional
    public void processarReavaliacao(Aluno aluno) {
        // RF2 & RN2: Coleta os últimos 10 desafios
        List<Pontuacao> ultimasPontuacoes = Pontuacao.find("aluno", Sort.by("dataConclusao").descending(), aluno)
                .page(0, DESAFIOS_PARA_AVALIACAO).list();

        if (ultimasPontuacoes.size() < DESAFIOS_PARA_AVALIACAO) {
            logger.warn("Aluno {} tem menos de {} desafios concluídos. Reavaliação abortada.", aluno.getId(), DESAFIOS_PARA_AVALIACAO);
            return;
        }

        long acertos = ultimasPontuacoes.stream().filter(Pontuacao::getAcertou).count();
        double percentualAcerto = (double) acertos / DESAFIOS_PARA_AVALIACAO;

        // RF3: Simulação de Inferência
        NivelCompetencia novoNivel = simularInferenciaIA(percentualAcerto);
        NivelCompetencia nivelAnterior = aluno.getNivelAtual();

        if (novoNivel != null && !novoNivel.equals(nivelAnterior)) {
            logger.info("Reavaliação para Aluno {}: Nível antigo '{}', Novo Nível '{}'", aluno.getId(), nivelAnterior.getDescricao(), novoNivel.getDescricao());

            // RN3: Log de Auditoria
            HistoricoNivel historico = new HistoricoNivel();
            historico.setAluno(aluno);
            historico.setNivelAnterior(nivelAnterior);
            historico.setNivelNovo(novoNivel);
            historico.setTipoReavaliacao("IA_AUTOMATICA");
            historico.setDataMudanca(new Date());
            historico.setJustificativa("Reavaliação automática da IA");
            // O campo 'voluntario' pode ser nulo para alterações automáticas
            historico.persist();

            // RF4: Atualização de Nível
            aluno.setNivelAtual(novoNivel);
            aluno.persist();

            // RF5: Ajuste de Trilha
            trilhaService.ativarNovaTrilha(aluno);

        } else {
            logger.info("Nível do aluno {} mantido após reavaliação.", aluno.getId());
        }
    }

    private NivelCompetencia simularInferenciaIA(double percentualAcerto) {
        // Lógica de negócio para determinar o nível com base no percentual de acerto
        if (percentualAcerto >= 0.8) {
            return NivelCompetencia.findById(3L); // Avançado
        } else if (percentualAcerto >= 0.5) {
            return NivelCompetencia.findById(2L); // Médio
        } else {
            return NivelCompetencia.findById(1L); // Básico
        }
    }
}