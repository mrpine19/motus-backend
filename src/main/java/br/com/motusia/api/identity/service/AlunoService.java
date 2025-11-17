package br.com.motusia.api.identity.service;

import br.com.motusia.api.identity.dto.AjusteNivelRequestDto;
import br.com.motusia.api.identity.dto.AlunoCreateDTO;
import br.com.motusia.api.identity.dto.AlunoUpdateDTO;
import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.identity.model.Usuario;
import br.com.motusia.api.identity.model.Voluntario;
import br.com.motusia.api.learning.model.NivelCompetencia;
import br.com.motusia.api.learning.model.Turma;
import br.com.motusia.api.progress.model.HistoricoNivel;
import br.com.motusia.api.progress.service.TrilhaService;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@ApplicationScoped
public class AlunoService {

    private static final Logger logger = LoggerFactory.getLogger(AlunoService.class);

    @Inject
    TrilhaService trilhaService;

    @Transactional
    public Aluno criarAluno(AlunoCreateDTO dto) {
        Turma turma = Turma.findById(dto.getIdTurma());
        if (turma == null) {
            logger.error("Turma não encontrada com o ID: {}", dto.getIdTurma());
            throw new NotFoundException("Turma não encontrada com o ID: " + dto.getIdTurma());
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNomeCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setSenhaHash(BcryptUtil.bcryptHash(dto.getSenha()));
        usuario.setTipo("ALUNO");
        usuario.setAtivo("S");
        usuario.persist();

        Aluno aluno = new Aluno();
        aluno.setUsuario(usuario);
        aluno.setTurma(turma);
        aluno.setDataCadastro(new Date());
        aluno.setStreakAtual(0);
        aluno.persist();

        return aluno;
    }

    @Transactional
    public Aluno atualizarAluno(Long alunoId, AlunoUpdateDTO dto) {
        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            logger.error("Aluno não encontrado com o ID: {}", alunoId);
            throw new NotFoundException("Aluno não encontrado com o ID: " + alunoId);
        }

        Turma turma = Turma.findById(dto.getIdTurma());
        if (turma == null) {
            logger.error("Turma não encontrada com o ID: {}", dto.getIdTurma());
            throw new NotFoundException("Turma não encontrada com o ID: " + dto.getIdTurma());
        }

        Usuario usuario = aluno.getUsuario();
        usuario.setNome(dto.getNomeCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.persist();

        aluno.setTurma(turma);
        aluno.persist();

        return aluno;
    }

    @Transactional
    public void inativandoAluno(Long alunoId) {
        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            logger.error("Aluno não encontrado com o ID: {}", alunoId);
            throw new NotFoundException("Aluno não encontrado com o ID: " + alunoId);
        }

        Usuario usuario = aluno.getUsuario();
        usuario.setAtivo("N");
        usuario.persist();
    }

    @Transactional
    public void ajustarNivel(AjusteNivelRequestDto dto) {
        if (dto.getJustificativa() == null || dto.getJustificativa().isBlank()) {
            throw new BadRequestException("A justificativa é obrigatória para a intervenção manual.");
        }

        Aluno aluno = Aluno.findById(dto.getAlunoId());
        if (aluno == null) {
            throw new NotFoundException("Aluno não encontrado com o ID: " + dto.getAlunoId());
        }

        NivelCompetencia nivelNovo = NivelCompetencia.findById(dto.getNovoNivelId());
        if (nivelNovo == null) {
            throw new NotFoundException("Nível de competência não encontrado com o ID: " + dto.getNovoNivelId());
        }

        Voluntario voluntario = Voluntario.findById(dto.getVoluntarioId());
        if (voluntario == null) {
            throw new NotFoundException("Voluntário não encontrado com o ID: " + dto.getVoluntarioId());
        }

        NivelCompetencia nivelAnterior = aluno.getNivelAtual();

        HistoricoNivel historico = new HistoricoNivel();
        historico.setAluno(aluno);
        historico.setNivelAnterior(nivelAnterior);
        historico.setNivelNovo(nivelNovo);
        historico.setDataMudanca(new Date());
        historico.setJustificativa(dto.getJustificativa());
        historico.setTipoReavaliacao("MANUAL");
        historico.persist();

        aluno.setNivelAtual(nivelNovo);
        aluno.persist();

        trilhaService.ativarNovaTrilha(aluno);

        logger.info("Ajuste de nível para o aluno {} concluído pelo voluntário {}. Justificativa: {}",
                aluno.getId(), voluntario.getId(), dto.getJustificativa());
    }
}