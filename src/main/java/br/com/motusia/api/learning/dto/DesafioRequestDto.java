package br.com.motusia.api.learning.dto;

import java.util.List;

public class DesafioRequestDto {

    private List<DesafioDto> desafios;

    public List<DesafioDto> getDesafios() {
        return desafios;
    }

    public void setDesafios(List<DesafioDto> desafios) {
        this.desafios = desafios;
    }
}