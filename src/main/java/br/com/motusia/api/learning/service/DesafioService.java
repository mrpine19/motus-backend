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

            Voluntario voluntario = Voluntario.findById(dto.getVoluntarioId());
            if (voluntario == null) {
                logger.error("Voluntário criador %d não encontrado.", dto.getVoluntarioId());
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

            if (dto.getNivelDificuldadeId() != null) {
                NivelCompetencia nivel = NivelCompetencia.findById(dto.getNivelDificuldadeId());
                desafio.setNivelDificuldade(nivel);
            }

            desafio.setCriadoPor(voluntario);

            desafio.persist();
        }
    }
}