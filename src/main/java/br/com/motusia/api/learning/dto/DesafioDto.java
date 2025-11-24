package br.com.motusia.api.learning.dto;

public class DesafioDto {

    private Long id;
    private String titulo;
    private String descricao;
    private String areaCompetencia;
    private String respostaCorreta;
    private String feedbackExplicacao;
    private String nivelDificuldade;
    private String nomeVoluntario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getNivelDificuldade() {
        return nivelDificuldade;
    }

    public void setNivelDificuldade(String nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }

    public String getNomeVoluntario() {
        return nomeVoluntario;
    }

    public void setNomeVoluntario(String nomeVoluntario) {
        this.nomeVoluntario = nomeVoluntario;
    }
}