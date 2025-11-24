package br.com.motusia.api.identity.service;

import br.com.motusia.api.identity.dto.AjusteNivelDto;
import br.com.motusia.api.identity.dto.AlunoCreateDTO;
import br.com.motusia.api.identity.dto.AlunoDTO;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class AlunoService {

    private static final Logger logger = LoggerFactory.getLogger(AlunoService.class);

    @Inject
    TrilhaService trilhaService;

    @Transactional
    public AlunoDTO criarAluno(AlunoCreateDTO dto) {
        Turma turma = Turma.find("nome", dto.getNomeTurma()).firstResult();
        if (turma == null) {
            logger.error("Turma com nome '{}' não encontrada", dto.getNomeTurma());
            throw new NotFoundException("Turma com nome '"+dto.getNomeTurma()+"' não encontrada");
        }

        NivelCompetencia nivelAtual = NivelCompetencia.find("descricao", dto.getNivelAtual()).firstResult();
        if (nivelAtual == null) {
            logger.error("Nível de competência com descrição '{}' não encontrada", dto.getNivelAtual());
            throw new NotFoundException("Nível de competência com descrição '"+dto.getNivelAtual()+"' não encontrada");
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
        aluno.setNivelAtual(nivelAtual);
        aluno.persist();

        return new AlunoDTO(aluno);
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
    public void ajustarNivelManualmente(Long alunoId, Long voluntarioId, AjusteNivelDto dto) {
        if (dto.getJustificativa() == null || dto.getJustificativa().isBlank()) {
            throw new BadRequestException("A justificativa é obrigatória para a intervenção manual.");
        }

        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            throw new NotFoundException("Aluno não encontrado com o ID: " + alunoId);
        }

        NivelCompetencia nivelNovo = NivelCompetencia.findById(dto.getNovoNivelId());
        if (nivelNovo == null) {
            throw new NotFoundException("Nível de competência não encontrado com o ID: " + dto.getNovoNivelId());
        }

        Voluntario voluntario = Voluntario.findById(voluntarioId);
        if (voluntario == null) {
            throw new NotFoundException("Voluntário não encontrado com o ID: " + voluntarioId);
        }

        NivelCompetencia nivelAntigo = aluno.getNivelAtual();

        // RN3: Registro de Auditoria (CORRIGIDO)
        HistoricoNivel historico = new HistoricoNivel();
        historico.setAluno(aluno);
        historico.setNivelAnterior(nivelAntigo);
        historico.setNivelNovo(nivelNovo);
        historico.setJustificativa(dto.getJustificativa());

        // --- LINHAS FALTANTES CORRIGIDAS ---
        historico.setTipoReavaliacao("MANUAL"); // Define o tipo (NOT NULL)
        historico.setDataMudanca(new Date()); // Define a data (NOT NULL no DDL)
        // --- FIM DA CORREÇÃO ---

        historico.persist();

        // RF4 & RN1: Persistência da Mudança e Superposição
        aluno.setNivelAtual(nivelNovo);
        aluno.persist();

        // RN4: Ativação de Trilha
        trilhaService.ativarNovaTrilha(aluno);

        logger.info("Nível do aluno {} ajustado manualmente para '{}' pelo voluntário {}. Justificativa: {}",
                aluno.getId(), nivelNovo.getDescricao(), voluntario.getId(), dto.getJustificativa());
    }

    public List<AlunoDTO> listaTodasOsALunos() {
        List<Aluno> alunos = Aluno.listAll();
        List<AlunoDTO> alunoDTOS = new ArrayList<>();

        for (Aluno aluno : alunos) {
            AlunoDTO alunoDTO = new AlunoDTO();
            alunoDTO.setId(aluno.getId());
            alunoDTO.setNome(aluno.getUsuario().getNome());
            alunoDTO.setEmail(aluno.getUsuario().getEmail());
            alunoDTO.setNivelAtual(aluno.getNivelAtual().getDescricao());
            alunoDTO.setNomeTurma(aluno.getTurma().getNome());
            alunoDTOS.add(alunoDTO);
        }
        return alunoDTOS;
    }
}