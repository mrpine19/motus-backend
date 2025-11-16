package br.com.motusia.api.identity.model;

import br.com.motusia.api.learning.model.NivelCompetencia;
import br.com.motusia.api.learning.model.Turma;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "TB_MOT_ALUNO")
public class Aluno extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "sqMotAluno",
            sequenceName = "SQ_MOT_ALUNO",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqMotAluno")
    @Column(name = "id_aluno")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "streak_atual")
    private Integer streakAtual;

    @Column(name = "data_cadastro")
    private Date dataCadastro;

    @Column(name = "ultimo_login")
    private Date ultimoLogin;

    @ManyToOne
    @JoinColumn(name = "id_turma")
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "id_nivel_atual")
    private NivelCompetencia nivelAtual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getStreakAtual() {
        return streakAtual;
    }

    public void setStreakAtual(Integer streakAtual) {
        this.streakAtual = streakAtual;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Date getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(Date ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public NivelCompetencia getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(NivelCompetencia nivelAtual) {
        this.nivelAtual = nivelAtual;
    }
}