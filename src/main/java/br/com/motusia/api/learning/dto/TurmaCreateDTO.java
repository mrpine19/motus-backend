package br.com.motusia.api.learning.dto;

public class TurmaCreateDTO {
    private String nomeDaTurma;
    private String descricao;
    private String nomeVoluntarioResponsavel;

    public String getNomeDaTurma() {
        return nomeDaTurma;
    }

    public void setNomeDaTurma(String nomeDaTurma) {
        this.nomeDaTurma = nomeDaTurma;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeVoluntarioResponsavel() {
        return nomeVoluntarioResponsavel;
    }

    public void setNomeVoluntarioResponsavel(String nomeVoluntarioResponsavel) {
        this.nomeVoluntarioResponsavel = nomeVoluntarioResponsavel;
    }
}
