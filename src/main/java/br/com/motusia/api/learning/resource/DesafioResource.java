package br.com.motusia.api.learning.resource;

import br.com.motusia.api.learning.dto.DesafioRequestDto;
import br.com.motusia.api.learning.service.DesafioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/desafios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DesafioResource {

    @Inject
    DesafioService desafioService;

    @POST
    public Response criarDesafios(DesafioRequestDto desafioRequestDto) {
        desafioService.criarDesafios(desafioRequestDto);
        return Response.status(Response.Status.CREATED).build();
    }
}