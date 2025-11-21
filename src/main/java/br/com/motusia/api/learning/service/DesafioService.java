package br.com.motusia.api.learning.service;

import br.com.motusia.api.identity.model.Voluntario;
import br.com.motusia.api.learning.dto.DesafioDto;
import br.com.motusia.api.learning.dto.DesafioRequestDto;
import br.com.motusia.api.learning.model.AreaCompetencia;
import br.com.motusia.api.learning.model.Desafio;
import br.com.motusia.api.learning.model.NivelCompetencia;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DesafioService {

    private static final Logger logger = LoggerFactory.getLogger(DesafioService.class);

    @Transactional
    public void criarDesafios(DesafioRequestDto desafioRequestDto) {
        if (desafioRequestDto == null || desafioRequestDto.getDesafios() == null || desafioRequestDto.getDesafios().isEmpty()) {
            logger.error("A lista de desafios não pode ser nula ou vazia.");
            throw new BadRequestException("A lista de desafios não pode ser nula ou vazia.");
        }

        for (DesafioDto dto : desafioRequestDto.getDesafios()) {
            if (dto.getRespostaCorreta() == null || dto.getRespostaCorreta().trim().isEmpty()) {
                logger.error("O campo Resposta Correta é obrigatório.");
                throw new BadRequestException("O campo Resposta Correta é obrigatório.");
            }

            Voluntario voluntario = Voluntario.findById(dto.getNomeVoluntario());
            if (voluntario == null) {
                logger.error("Voluntário criador %d não encontrado.", dto.getNomeVoluntario());
                throw new NotFoundException("Voluntário criador não encontrado.");
            }

            AreaCompetencia areaCompetencia = AreaCompetencia.find("codigo = ?1", dto.getAreaCompetencia()).firstResult();

            Desafio desafio = new Desafio();
            desafio.setTitulo(dto.getTitulo());
            desafio.setDescricao(dto.getDescricao());
            desafio.setAreaCompetencia(areaCompetencia);
            desafio.setRespostaCorreta(dto.getRespostaCorreta());
            desafio.setFeedbackExplicacao(dto.getFeedbackExplicacao());
            desafio.setAtivo("S");

            if (dto.getNivelDificuldade() != null) {
                NivelCompetencia nivel = NivelCompetencia.findById(dto.getNivelDificuldade());
                desafio.setNivelDificuldade(nivel);
            }

            desafio.setCriadoPor(voluntario);

            desafio.persist();
        }
    }

    public List<DesafioRequestDto> listaTodasAsAula() {
        List<Desafio> desafios = Desafio.list("ativo = ?1", "S");
        List<DesafioDto> dtos = new ArrayList<>();

        for (Desafio desafio : desafios) {
            DesafioDto dto = new DesafioDto();
            dto.setId(desafio.getId());
            dto.setTitulo(desafio.getTitulo());
            dto.setDescricao(desafio.getDescricao());

            if (desafio.getAreaCompetencia() != null) {
                dto.setAreaCompetencia(desafio.getAreaCompetencia().getCodigo());
            }

            dto.setRespostaCorreta(desafio.getRespostaCorreta());
            dto.setFeedbackExplicacao(desafio.getFeedbackExplicacao());

            if (desafio.getNivelDificuldade() != null) {
                dto.setNivelDificuldade(desafio.getNivelDificuldade().getDescricao());
            }

            if (desafio.getCriadoPor() != null) {
                dto.setNomeVoluntario(desafio.getCriadoPor().getUsuario().getNome());
            }
            dtos.add(dto);
        }

        DesafioRequestDto requestDto = new DesafioRequestDto();
        requestDto.setDesafios(dtos);

        List<DesafioRequestDto> result = new ArrayList<>();
        result.add(requestDto);

        return result;
    }

    public DesafioDto buscarDesafio(Long id) {
        Desafio desafio = Desafio.findById(id);

        if (desafio == null) {
            logger.error("Desafio não encontrado com ID: {}", id);
            throw new NotFoundException("Desafio não encontrado com ID: " + id);
        }

        DesafioDto desafioDto = new DesafioDto();
        desafioDto.setId(desafio.getId());
        desafioDto.setTitulo(desafio.getTitulo());
        desafioDto.setDescricao(desafio.getDescricao());
        desafioDto.setAreaCompetencia(desafio.getAreaCompetencia().getCodigo());
        desafioDto.setRespostaCorreta(desafio.getRespostaCorreta());
        desafioDto.setFeedbackExplicacao(desafio.getFeedbackExplicacao());
        desafioDto.setNivelDificuldade(desafio.getNivelDificuldade().getDescricao());
        desafioDto.setNomeVoluntario(desafio.getCriadoPor().getUsuario().getNome());

        return desafioDto;
    }
}
