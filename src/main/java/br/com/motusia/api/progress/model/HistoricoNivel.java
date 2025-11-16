package br.com.motusia.api.progress.model;

import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.learning.model.NivelCompetencia;
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
@Table(name = "TB_MOT_HISTORICO_NIVEL")
public class HistoricoNivel extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "sqMotHistorico",
            sequenceName = "SQ_MOT_HISTORICO",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqMotHistorico")
    @Column(name = "id_historico")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "id_nivel_anterior")
    private NivelCompetencia nivelAnterior;

    @ManyToOne
    @JoinColumn(name = "id_nivel_novo")
    private NivelCompetencia nivelNovo;

    @Column(name = "tipo_reavaliacao")
    private String tipoReavaliacao;

    @Column(name = "data_mudanca")
    private Date dataMudanca;
    
    @Column(name = "justificativa")
    private String justificativa;

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

    public NivelCompetencia getNivelAnterior() {
        return nivelAnterior;
    }

    public void setNivelAnterior(NivelCompetencia nivelAnterior) {
        this.nivelAnterior = nivelAnterior;
    }

    public NivelCompetencia getNivelNovo() {
        return nivelNovo;
    }

    public void setNivelNovo(NivelCompetencia nivelNovo) {
        this.nivelNovo = nivelNovo;
    }

    public String getTipoReavaliacao() {
        return tipoReavaliacao;
    }

    public void setTipoReavaliacao(String tipoReavaliacao) {
        this.tipoReavaliacao = tipoReavaliacao;
    }

    public Date getDataMudanca() {
        return dataMudanca;
    }

    public void setDataMudanca(Date dataMudanca) {
        this.dataMudanca = dataMudanca;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}