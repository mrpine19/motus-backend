package br.com.motusia.api.identity.service;

import br.com.motusia.api.identity.model.Voluntario;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class VoluntarioService {

    public List<String> listaNomeDeTodosVoluntarios(){
        List<Voluntario> voluntarios = Voluntario.listAll();
        List<String> listaNomesVoluntarios = new ArrayList<>();

        for (Voluntario voluntario : voluntarios) {
            listaNomesVoluntarios.add(voluntario.getUsuario().getNome());
        }

        return listaNomesVoluntarios;
    }
}
