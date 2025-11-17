package br.com.motusia.api.learning.dto;

public class DesafioDto {

    private String titulo;
    private String descricao;
    private String areaCompetencia;
    private String respostaCorreta;
    private String feedbackExplicacao;
    private Long nivelDificuldadeId;
    private Long voluntarioId;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAreaCompetencia() {
        return areaCompetencia;
    }

    public void setAreaCompetencia(String areaCompetencia) {
        this.areaCompetencia = areaCompetencia;
    }

    public String getRespostaCorreta() {
        return respostaCorreta;
    }

    public void setRespostaCorreta(String respostaCorreta) {
        this.respostaCorreta = respostaCorreta;
    }

    public String getFeedbackExplicacao() {
        return feedbackExplicacao;
    }

    public void setFeedbackExplicacao(String feedbackExplicacao) {
        this.feedbackExplicacao = feedbackExplicacao;
    }

    public Long getNivelDificuldadeId() {
        return nivelDificuldadeId;
    }

    public void setNivelDificuldadeId(Long nivelDificuldadeId) {
        this.nivelDificuldadeId = nivelDificuldadeId;
    }

    public Long getVoluntarioId() {
        return voluntarioId;
    }

    public void setVoluntarioId(Long voluntarioId) {
        this.voluntarioId = voluntarioId;
    }
}