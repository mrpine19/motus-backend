package br.com.motusia.api.identity.dto;

import java.util.List;

public class TurmaDashboardDto {
    private Long id;
    private String nome;
    private List<AlunoDashboardDto> alunos;
    private List<DificuldadeAreaDto> maioresDificuldades;

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

    public List<AlunoDashboardDto> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<AlunoDashboardDto> alunos) {
        this.alunos = alunos;
    }

    public List<DificuldadeAreaDto> getMaioresDificuldades() {
        return maioresDificuldades;
    }

    public void setMaioresDificuldades(List<DificuldadeAreaDto> maioresDificuldades) {
        this.maioresDificuldades = maioresDificuldades;
    }
}