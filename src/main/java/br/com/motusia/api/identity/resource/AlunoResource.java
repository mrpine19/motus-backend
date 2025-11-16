package br.com.motusia.api.identity.resource;

import br.com.motusia.api.identity.dto.AlunoCreateDTO;
import br.com.motusia.api.identity.dto.AlunoUpdateDTO;
import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.identity.service.AlunoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/alunos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlunoResource {

    @Inject
    AlunoService alunoService;

    @POST
    public Response criarAluno(@Valid AlunoCreateDTO dto) {
        Aluno alunoCriado = alunoService.criarAluno(dto);
        return Response.status(Response.Status.CREATED).entity(alunoCriado).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizarAluno(@PathParam("id") Long id, @Valid AlunoUpdateDTO dto) {
        Aluno alunoAtualizado = alunoService.atualizarAluno(id, dto);
        return Response.ok(alunoAtualizado).build();
    }

    @PATCH
    @Path("/{id}/inativar")
    public Response inativarAluno(@PathParam("id") Long id) {
        alunoService.inativandoAluno(id);
        return Response.noContent().build();
    }
}