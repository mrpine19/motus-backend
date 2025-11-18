package br.com.motusia.api.identity.service;

import br.com.motusia.api.identity.dto.AlunoDashboardDto;
import br.com.motusia.api.identity.dto.DificuldadeAreaDto;
import br.com.motusia.api.identity.dto.TurmaDashboardDto;
import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.identity.model.Voluntario;
import br.com.motusia.api.learning.model.Turma;
import br.com.motusia.api.progress.model.HistoricoNivel;
import br.com.motusia.api.progress.model.Pontuacao;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class DashboardService {

    private static final int DESAFIOS_PARA_ANALISE_DIFICULDADE = 20;
    private static final int DESAFIOS_PARA_TAXA_ACERTO = 10;

    public List<TurmaDashboardDto> getDashboardData(Long voluntarioId) {
        // RF1 & RN1: Validação de segurança
        Voluntario voluntario = Voluntario.findById(voluntarioId);
        if (voluntario == null) {
            throw new NotFoundException("Voluntário não encontrado.");
        }

        // RF2: Filtro por Turma do voluntário
        List<Turma> turmas = Turma.list("voluntarioResponsavel", voluntario);

        // Ajuste: Retornar lista vazia em vez de erro 404 se não tiver turmas é uma prática melhor de UI,
        // mas se o requisito pede erro, mantenha o throw.
        if (turmas.isEmpty()) {
            return Collections.emptyList();
        }

        return turmas.stream()
                .map(this::buildTurmaDashboardDto)
                .collect(Collectors.toList());
    }

    private TurmaDashboardDto buildTurmaDashboardDto(Turma turma) {
        TurmaDashboardDto dto = new TurmaDashboardDto();
        dto.setId(turma.getId());
        dto.setNome(turma.getNome());

        List<Aluno> alunos = Aluno.list("turma", turma);

        // Construção dos dados dos alunos
        dto.setAlunos(alunos.stream()
                .map(this::buildAlunoDashboardDto)
                .collect(Collectors.toList()));

        // Cálculo agregado da turma
        dto.setMaioresDificuldades(calcularMaioresDificuldades(alunos));

        return dto;
    }

    private AlunoDashboardDto buildAlunoDashboardDto(Aluno aluno) {
        AlunoDashboardDto dto = new AlunoDashboardDto();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getUsuario().getNome());

        // RF3 & RN2: Visualização do Nível
        if (aluno.getNivelAtual() != null) {
            dto.setNivelAtual(aluno.getNivelAtual().getDescricao());
        } else {
            dto.setNivelAtual("Não definido");
        }

        // RF5: Detalhe do Aluno - taxa de acerto (Query Individual - Aceitável para MVP)
        List<Pontuacao> ultimas10 = Pontuacao.find("aluno", Sort.by("dataConclusao").descending(), aluno)
                .page(0, DESAFIOS_PARA_TAXA_ACERTO).list();

        long acertos = ultimas10.stream().filter(Pontuacao::getAcertou).count();
        dto.setTaxaAcertoUltimos10(ultimas10.isEmpty() ? 0.0 : (double) acertos / ultimas10.size());

        // RF6: Indicador de Intervenção (Query Individual)
        long countIntervencoes = HistoricoNivel.count("aluno = ?1 and tipoReavaliacao = 'MANUAL'", aluno);
        dto.setTeveIntervencaoManual(countIntervencoes > 0);

        return dto;
    }

    private List<DificuldadeAreaDto> calcularMaioresDificuldades(List<Aluno> alunos) {
        if (alunos.isEmpty()) return Collections.emptyList();

        // RN3: Cálculo de Dificuldade em lote para a turma
        // Busca as últimas N interações de TODOS os alunos da turma para gerar o mapa de calor
        List<Pontuacao> pontuacoesDaTurma = Pontuacao.find("aluno in ?1", Sort.by("dataConclusao").descending(), alunos)
                .page(0, DESAFIOS_PARA_ANALISE_DIFICULDADE * alunos.size()).list();

        Map<String, List<Pontuacao>> porArea = pontuacoesDaTurma.stream()
                .filter(p -> p.getDesafio() != null && p.getDesafio().getAreaCompetencia() != null) // Proteção contra Null
                .collect(Collectors.groupingBy(p -> p.getDesafio().getAreaCompetencia())); // Agrupa pela descrição da área

        return porArea.entrySet().stream()
                .map(entry -> {
                    String area = entry.getKey();
                    List<Pontuacao> pontuacoesNaArea = entry.getValue();
                    long erros = pontuacoesNaArea.stream().filter(p -> !p.getAcertou()).count();
                    double taxaErro = pontuacoesNaArea.isEmpty() ? 0.0 : (double) erros / pontuacoesNaArea.size();
                    return new DificuldadeAreaDto(area, taxaErro);
                })
                // CORREÇÃO: Ordenar da maior taxa de erro para a menor
                .sorted(Comparator.comparing(DificuldadeAreaDto::getTaxaErro).reversed())
                .collect(Collectors.toList());
    }
}