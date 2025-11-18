package br.com.motusia.api.progress.service;

import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.learning.model.Desafio;
import br.com.motusia.api.learning.model.NivelCompetencia;
import br.com.motusia.api.progress.model.HistoricoNivel;
import br.com.motusia.api.progress.model.Pontuacao;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@ApplicationScoped
public class TrilhaService {

    private static final Logger logger = LoggerFactory.getLogger(TrilhaService.class);

    // --- Constantes de Regra de Negócio ---

    // US 4 (Reavaliação): Gatilho de 5 desafios
    private static final int GATILHO_REAVALIACAO = 5;
    // US 4 (Simulação IA): Limites de performance
    private static final double LIMITE_AVANCAR = 0.80; // 80% de acerto para subir
    private static final double LIMITE_RECUAR = 0.40;  // 40% de acerto para descer

    // US 2 (RN2): Balanceamento da Trilha
    private static final Map<String, Integer> QUOTAS_POR_AREA = Map.of(
            "LOGICA", 3,
            "PORTUGUES", 3,
            "RESOLUCAO", 2
    );

    /**
     * OBSERVADOR (LISTENER) DA US 4:
     * É disparado pelo PontuacaoService (US 2) quando o gatilho de reavaliação é atingido.
     * Executa a lógica de IA (simulada) e atualiza o nível do aluno.
     */
    @Transactional // Obrigatório para acessar o DB (find/persist)
    public void onReavaliacao(@Observes Aluno aluno) {
        logger.info("Evento de reavaliação recebido para o Aluno ID: %d. Calculando nível...", aluno.getId());

        // 1. Coleta os dados para a IA (RF2)
        List<Pontuacao> ultimasPontuacoes = Pontuacao.find(
                "aluno = ?1 ORDER BY dataConclusao DESC", aluno
        ).page(0, GATILHO_REAVALIACAO).list();

        // 2. Simulação da Inferência (RF3)
        long acertos = ultimasPontuacoes.stream().filter(Pontuacao::getAcertou).count();
        double percentualAcerto = (double) acertos / ultimasPontuacoes.size();

        NivelCompetencia nivelSugerido = determinarNovoNivel(aluno.getNivelAtual(), percentualAcerto);

        // 3. Persistência e Ativação (RN3, RF4)
        if (nivelSugerido != null && !nivelSugerido.equals(aluno.getNivelAtual())) {

            // RN3: Registro de Auditoria (Persiste o log)
            HistoricoNivel historico = new HistoricoNivel();
            historico.setAluno(aluno);
            historico.setNivelAnterior(aluno.getNivelAtual());
            historico.setNivelNovo(nivelSugerido);
            historico.setDataMudanca(new Date());
            historico.setTipoReavaliacao("IA_AUTOMATICA");
            historico.persist();

            // RF4: Atualiza o Nível do Aluno
            aluno.setNivelAtual(nivelSugerido);
            aluno.persist();
            logger.info("Nível do Aluno %d atualizado para %s (%.2f%% acerto).", aluno.getId(), nivelSugerido.getCodigo(), percentualAcerto * 100);

            // 4. RN4: Ativação da Nova Trilha
            // Chama o método que busca os novos desafios para o nível atualizado
            ativarNovaTrilha(aluno);

        } else {
            logger.info("Nível do Aluno %d mantido em %s (%.2f%% acerto).", aluno.getId(), aluno.getNivelAtual().getCodigo(), percentualAcerto * 100);
        }
    }

    /**
     * WORKER (RN4):
     * Chamado pelo NivelService (Ajuste Manual) ou pelo onReavaliacao (IA Automática).
     * Prepara a próxima lista de desafios para o aluno (US 2).
     */
    @Transactional // Obrigatório para acessar o DB (find)
    public void ativarNovaTrilha(Aluno aluno) {
        logger.info("Ativando nova trilha de desafios para o aluno ID {}.", aluno.getId());

        List<Desafio> novaTrilha = new ArrayList<>();
        NivelCompetencia novoNivel = aluno.getNivelAtual();
        // ... (Validação de Nível) ...
        Long idNivelParaBusca = novoNivel.getId();

        // 1. BUSCAR DESAFIOS ADEQUADOS E BALANCEADOS (RN2)
        for (Map.Entry<String, Integer> quota : QUOTAS_POR_AREA.entrySet()) {
            String area = quota.getKey();
            int quantidade = quota.getValue();

            // CORREÇÃO: Passar a STRING "S" para o status 'ativo', que agora é VARCHAR2 no DB.
            PanacheQuery<Desafio> query = Desafio.find(
                    "nivelDificuldade.id = ?1 AND areaCompetencia = ?2 AND ativo = ?3",
                    idNivelParaBusca,
                    area,
                    "S" // CORREÇÃO APLICADA: Passar o String literal 'S'
            );

            List<Desafio> desafios = query.page(0, 100).list();

            // 2. APLICAÇÃO DA QUOTA (Seleção de uma amostra)
            if (desafios.size() >= quantidade) {
                List<Desafio> selecionados = new ArrayList<>(desafios.subList(0, quantidade));
                novaTrilha.addAll(selecionados);
                logger.info("  -> Adicionados {} desafios da área {}.", selecionados.size(), area);
            } else {
                logger.warn("  -> ATENÇÃO: Apenas {} desafios disponíveis para a área {}. (Abaixo da quota de {})", desafios.size(), area, quantidade);
                novaTrilha.addAll(desafios); // Adiciona os que encontrou
            }
        }

        // 3. Log final (A trilha está pronta para ser consumida pela US 2 no Front-End)
        logger.info("Nova trilha de desafios de %d itens ativada com sucesso para o aluno {}. Pronta para consumo.", novaTrilha.size(), aluno.getId());
    }

    /**
     * LÓGICA DE IA (SIMULADA):
     * Determina o novo nível com base na performance.
     */
    private NivelCompetencia determinarNovoNivel(NivelCompetencia nivelAtual, double percentualAcerto) {
        Optional<NivelCompetencia> novoNivel = Optional.empty();

        if (percentualAcerto >= LIMITE_AVANCAR) {
            // Sugestão de avançar: Buscar o próximo nível na ordem (Ordem + 1)
            novoNivel = NivelCompetencia.find("ordem", nivelAtual.getOrdem() + 1).firstResultOptional();
        } else if (percentualAcerto <= LIMITE_RECUAR && nivelAtual.getOrdem() > 1) {
            // Sugestão de recuar: Buscar o nível anterior (Ordem - 1)
            novoNivel = NivelCompetencia.find("ordem", nivelAtual.getOrdem() - 1).firstResultOptional();
        }

        // Retorna o novo nível (se encontrado) ou o nível atual se nenhuma mudança for sugerida
        return novoNivel.orElse(nivelAtual);
    }
}