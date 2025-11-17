package br.com.motusia.api.progress.service;

import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.learning.model.Desafio;
import br.com.motusia.api.learning.model.NivelCompetencia;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TrilhaService {

    private static final Logger logger = LoggerFactory.getLogger(TrilhaService.class);

    // Regra de Negócio (RN2): Define o balanceamento da nova trilha (ex: 8 desafios)
    private static final Map<String, Integer> QUOTAS_POR_AREA = Map.of(
            "LOGICA", 3,
            "PORTUGUES", 3,
            "RESOLUCAO", 2
    );

    @Transactional // Necessário para acessar o banco de dados
    public void ativarNovaTrilha(Aluno aluno) {
        logger.info("Ativando nova trilha de desafios para o aluno ID %d.", aluno.getId());

        List<Desafio> novaTrilha = new ArrayList<>();
        NivelCompetencia novoNivel = aluno.getNivelAtual();
        Long idNivelParaBusca = novoNivel.getId();

        // 1. BUSCAR DESAFIOS ADEQUADOS E BALANCEADOS (RN2)
        for (Map.Entry<String, Integer> quota : QUOTAS_POR_AREA.entrySet()) {
            String area = quota.getKey();
            int quantidade = quota.getValue();

            // Consulta Panache (Busca pelo ID do Nível e Área - Correção ORA-01722)
            // Assumindo que a entidade Desafio tem um campo FK chamado 'idNivelDificuldade' para o ID do Nível
            PanacheQuery<Desafio> query = Desafio.find(
                    // CORREÇÃO: Usar o caminho do objeto para o ID (.id)
                    "nivelDificuldade.id = ?1 AND areaCompetencia = ?2 AND ativo = ?3",
                    idNivelParaBusca,
                    area,
                    "S"
            );

            List<Desafio> desafios = query.page(0, 100).list();

            // 2. APLICAÇÃO DA QUOTA (Seleção de uma amostra)
            if (desafios.size() >= quantidade) {
                // Seleciona a quota necessária (assumindo que os primeiros são suficientes para o MVP)
                List<Desafio> selecionados = new ArrayList<>(desafios.subList(0, quantidade));
                novaTrilha.addAll(selecionados);
                logger.info("  -> Adicionados {} desafios da área {}.", selecionados.size(), area);
            } else {
                logger.warn("  -> ATENÇÃO: Apenas  {} desafios disponíveis para a área {}. (Abaixo da quota de {})", desafios.size(), area, quantidade);
            }
        }

        logger.info("Nova trilha de desafios de {} itens ativada com sucesso para o aluno {}. Pronta para consumo.", novaTrilha.size(), aluno.getId());
    }
}