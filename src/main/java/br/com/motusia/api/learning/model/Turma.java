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
import java.util.Date;

@Entity
@Table(name = "TB_MOT_TURMA")
public class Turma extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "sqMotTurma",
            sequenceName = "SQ_MOT_TURMA",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqMotTurma")
    @Column(name = "id_turma")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_voluntario")
    private Voluntario voluntarioResponsavel;

    @Column(name = "data_criacao")
    private Date dataCriacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Voluntario getVoluntarioResponsavel() {
        return voluntarioResponsavel;
    }

    public void setVoluntarioResponsavel(Voluntario voluntarioResponsavel) {
        this.voluntarioResponsavel = voluntarioResponsavel;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}