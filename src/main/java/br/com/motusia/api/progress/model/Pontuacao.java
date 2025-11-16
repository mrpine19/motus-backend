package br.com.motusia.api.progress.model;

import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.learning.model.Desafio;
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
import java.util.Date;

@Entity
@Table(name = "TB_MOT_PONTUACAO")
public class Pontuacao extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "sqMotPontuacao",
            sequenceName = "SQ_MOT_PONTUACAO",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqMotPontuacao")
    @Column(name = "id_pontuacao")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "id_desafio")
    private Desafio desafio;

    @Column(name = "pontos")
    private Integer pontos;

    @Column(name = "tempo_segundos")
    private Double tempoGastoSegundos;

    @Column(name = "acertou")
    private Boolean acertou;

    @Column(name = "data_conclusao")
    private Date dataConclusao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public Double getTempoGastoSegundos() {
        return tempoGastoSegundos;
    }

    public void setTempoGastoSegundos(Double tempoGastoSegundos) {
        this.tempoGastoSegundos = tempoGastoSegundos;
    }

    public Boolean getAcertou() {
        return acertou;
    }

    public void setAcertou(Boolean acertou) {
        this.acertou = acertou;
    }

    public Date getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
}