package br.com.motusia.api.identity.dto;

import br.com.motusia.api.identity.model.Aluno;

public class AlunoDTO {
    private Long id;
    private String nome;
    private String email;
    private String nivelAtual;
    private String nomeTurma;

    public AlunoDTO() {
    }

    public AlunoDTO(Aluno aluno) {
        this.id = aluno.getId();
        this.nome = aluno.getUsuario().getNome();
        this.email = aluno.getUsuario().getEmail();
        this.nivelAtual = aluno.getNivelAtual().getDescricao();
        this.nomeTurma = aluno.getTurma().getNome();
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(String nivelAtual) {
        this.nivelAtual = nivelAtual;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }
}
