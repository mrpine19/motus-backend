package br.com.motusia.api.learning.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "TB_MOT_AREA_COMPETENCIA")
public class AreaCompetencia extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "sqMotAreaCompetencia",
            sequenceName = "SQ_MOT_AREA_COMPETENCIA",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqMotAreaCompetencia")
    @Column(name = "id_area")
    private Long idAreaCompetencia;

    @Column(name="codigo")
    private String codigo;

    @Column(name="descricao")
    private String descricao;

    public Long getIdAreaCompetencia() {
        return idAreaCompetencia;
    }

    public void setIdAreaCompetencia(Long idAreaCompetencia) {
        this.idAreaCompetencia = idAreaCompetencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
