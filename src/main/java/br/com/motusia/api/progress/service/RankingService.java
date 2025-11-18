package br.com.motusia.api.progress.service;

import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.progress.dto.RankingDto;
import br.com.motusia.api.progress.model.Pontuacao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RankingService {

    @Transactional // A consulta DQL/READ deve ser transacional
    public List<RankingDto> calcularRankingSemanal(Long turmaId) {
        // RN2: Define o período da semana atual (de segunda a domingo)
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fimSemana = hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // Conversão para Date (necessário para a consulta Panache se a data no DB for Date)
        Date dataInicio = Date.from(inicioSemana.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dataFim = Date.from(fimSemana.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // RF1 e RN1: Filtra alunos pela turma
        List<Aluno> alunosDaTurma = Aluno.list("turma.id", turmaId);

        List<RankingDto> ranking = alunosDaTurma.stream().map(aluno -> {

            // RF2 e RN3: Consulta TODOS os pontos (acerto=1 ou erro=0) dentro do período.
            List<Pontuacao> pontuacoesNaSemana = Pontuacao.list(
                    "aluno = ?1 and dataConclusao >= ?2 and dataConclusao < ?3",
                    aluno, dataInicio, dataFim); // <--- CLÁUSULA 'acertou = true' REMOVIDA

            // Soma todos os pontos (incluindo os pontos por esforço)
            int pontuacaoTotal = pontuacoesNaSemana.stream().mapToInt(Pontuacao::getPontos).sum();

            // Soma o tempo gasto em todos os desafios concluídos
            double tempoTotal = pontuacoesNaSemana.stream().mapToDouble(Pontuacao::getTempoGastoSegundos).sum();

            return new RankingDto(aluno.getId(), aluno.getUsuario().getNome(), pontuacaoTotal, tempoTotal);
        }).collect(Collectors.toList());

        // RF3 e RN4: Ordena por pontuação (desc) e depois por tempo (asc)
        ranking.sort(Comparator.comparing(RankingDto::getPontuacaoTotal).reversed()
                .thenComparing(RankingDto::getTempoTotalGasto));

        // Atribui a posição no ranking (RF4)
        for (int i = 0; i < ranking.size(); i++) {
            ranking.get(i).setPosicao(i + 1);
        }

        return ranking;
    }
}