package br.com.motusia.api.learning.dto;

import br.com.motusia.api.learning.model.Turma;

public class TurmaDTO {
    private long id;
    private String nome;
    private String descricao;
    private String voluntarioResponsavel;

    public TurmaDTO(long id, String nome, String descricao, String voluntarioResponsavel) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.voluntarioResponsavel = voluntarioResponsavel;
    }

    public TurmaDTO(Turma turma) {
        this.id = turma.getId();
        this.nome = turma.getNome();
        this.descricao = turma.getDescricao();
        this.voluntarioResponsavel = turma.getVoluntarioResponsavel().getUsuario().getNome();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getVoluntarioResponsavel() {
        return voluntarioResponsavel;
    }

    public void setVoluntarioResponsavel(String voluntarioResponsavel) {
        this.voluntarioResponsavel = voluntarioResponsavel;
    }
}
