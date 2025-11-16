package br.com.motusia.api.analytics.model;

import br.com.motusia.api.identity.model.Patrocinador;
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
@Table(name = "TB_MOT_KPI_ESG")
public class KPI extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "sqMotKpi",
            sequenceName = "SQ_MOT_KPI",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqMotKpi")
    @Column(name = "id_kpi")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_patrocinador")
    private Patrocinador patrocinador;

    @Column(name = "periodo_referencia")
    private Date periodoReferencia;

    @Column(name = "total_alunos")
    private Integer totalAlunos;

    @Column(name = "taxa_evolucao")
    private Double taxaEvolucao;

    @Column(name = "horas_engajamento")
    private Double horasEngajamento;

    @Column(name = "custo_por_aluno")
    private Double custoPorAluno;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patrocinador getPatrocinador() {
        return patrocinador;
    }

    public void setPatrocinador(Patrocinador patrocinador) {
        this.patrocinador = patrocinador;
    }

    public Date getPeriodoReferencia() {
        return periodoReferencia;
    }

    public void setPeriodoReferencia(Date periodoReferencia) {
        this.periodoReferencia = periodoReferencia;
    }

    public Integer getTotalAlunos() {
        return totalAlunos;
    }

    public void setTotalAlunos(Integer totalAlunos) {
        this.totalAlunos = totalAlunos;
    }

    public Double getTaxaEvolucao() {
        return taxaEvolucao;
    }

    public void setTaxaEvolucao(Double taxaEvolucao) {
        this.taxaEvolucao = taxaEvolucao;
    }

    public Double getHorasEngajamento() {
        return horasEngajamento;
    }

    public void setHorasEngajamento(Double horasEngajamento) {
        this.horasEngajamento = horasEngajamento;
    }

    public Double getCustoPorAluno() {
        return custoPorAluno;
    }

    public void setCustoPorAluno(Double custoPorAluno) {
        this.custoPorAluno = custoPorAluno;
    }
}