package br.com.motusia.api.identity.resource;

import br.com.motusia.api.identity.service.VoluntarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/voluntarios")
@Produces(MediaType.APPLICATION_JSON)
public class VoluntarioResource {

    @Inject
    VoluntarioService voluntarioService;

    @GET
    @Path("/nomes")
    public Response listarNomesVoluntarios() {
        List<String> nomes = voluntarioService.listaNomeDeTodosVoluntarios();
        return Response.ok(nomes).build();
    }

}
