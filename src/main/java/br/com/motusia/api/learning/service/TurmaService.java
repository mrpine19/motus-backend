package br.com.motusia.api.learning.service;

import br.com.motusia.api.identity.model.Voluntario;
import br.com.motusia.api.identity.service.VoluntarioService;
import br.com.motusia.api.learning.dto.TurmaCreateDTO;
import br.com.motusia.api.learning.dto.TurmaDTO;
import br.com.motusia.api.learning.model.Turma;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class TurmaService {

    private static final Logger logger = LoggerFactory.getLogger(TurmaService.class);

    @Inject
    VoluntarioService voluntarioService;

    public List<TurmaDTO> listaTodasAsTurmas(){
        List<Turma> turmas = Turma.listAll();
        List<TurmaDTO> turmasDTO = new ArrayList<>();

        for (Turma turma : turmas) {
            TurmaDTO turmaDTO = new TurmaDTO(turma.getId(), turma.getNome(), turma.getDescricao(), turma.getVoluntarioResponsavel().getUsuario().getNome());
            turmasDTO.add(turmaDTO);
        }
        return turmasDTO;
    }

    @Transactional
    public TurmaDTO criarTurma(TurmaCreateDTO turmaCreateDTO) {

        List<String> nomeDosVoluntarios = voluntarioService.listaNomeDeTodosVoluntarios();

        if (!nomeDosVoluntarios.contains(turmaCreateDTO.getNomeVoluntarioResponsavel())) {
            logger.error("Aluno não encontrado voluntário de nome: {}", turmaCreateDTO.getNomeVoluntarioResponsavel());
            throw new NotFoundException("Aluno não encontrado voluntário de nome: " + turmaCreateDTO.getNomeVoluntarioResponsavel());
        }

        Voluntario voluntarioResponsavel = Voluntario.find("usuario.nome", turmaCreateDTO.getNomeVoluntarioResponsavel()).firstResult();

        Turma turma = new Turma();
        turma.setNome(turmaCreateDTO.getNomeDaTurma());
        turma.setDescricao(turmaCreateDTO.getDescricao());
        turma.setVoluntarioResponsavel(voluntarioResponsavel);
        turma.setDataCriacao(new Date());
        turma.persist();

        logger.info("Turma criada com sucesso: {}", turma);

        return new TurmaDTO(turma);
    }
}
