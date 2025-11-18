package br.com.motusia.api.progress.dto;

public class RankingDto {

    private int posicao;
    private String nomeAluno;
    private Long alunoId;
    private int pontuacaoTotal;
    private Double tempoTotalGasto;

    public RankingDto(Long alunoId, String nomeAluno, int pontuacaoTotal, Double tempoTotalGasto) {
        this.alunoId = alunoId;
        this.nomeAluno = nomeAluno;
        this.pontuacaoTotal = pontuacaoTotal;
        this.tempoTotalGasto = tempoTotalGasto;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public int getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public void setPontuacaoTotal(int pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public Double getTempoTotalGasto() {
        return tempoTotalGasto;
    }

    public void setTempoTotalGasto(Double tempoTotalGasto) {
        this.tempoTotalGasto = tempoTotalGasto;
    }
}