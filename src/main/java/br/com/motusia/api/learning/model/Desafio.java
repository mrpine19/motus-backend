package br.com.motusia.api.learning.model;

import br.com.motusia.api.identity.model.Voluntario;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_MOT_DESAFIO")
public class Desafio extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "sqMotDesafio",
            sequenceName = "SQ_MOT_DESAFIO",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqMotDesafio")
    @Column(name = "id_desafio")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "area_competencia")
    private String areaCompetencia;

    @Column(name = "resposta_correta")
    private String respostaCorreta;

    @Column(name = "feedback_explicacao")
    private String feedbackExplicacao;

    @Column(name = "ativo")
    private String ativo;

    @ManyToOne
    @JoinColumn(name = "id_nivel_dificuldade")
    private NivelCompetencia nivelDificuldade;

    @ManyToOne
    @JoinColumn(name = "id_voluntario_criador")
    private Voluntario criadoPor;

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

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public NivelCompetencia getNivelDificuldade() {
        return nivelDificuldade;
    }

    public void setNivelDificuldade(NivelCompetencia nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }

    public Voluntario getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(Voluntario criadoPor) {
        this.criadoPor = criadoPor;
    }
}