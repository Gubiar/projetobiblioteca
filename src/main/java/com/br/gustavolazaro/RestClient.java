package com.br.gustavolazaro;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.br.gustavolazaro.Biblioteca.QTD_LIVROS;

public class RestClient {

    private final String REST_URI = "https://server-17ah10xu3-gubiar.vercel.app/livros?qtd=" + QTD_LIVROS;

    private Client client = ClientBuilder.newClient();

    public List<Compartilhado> getLivrosFromAPI() {
        Response response = client
                .target(REST_URI)
                .path("")
                .request(MediaType.APPLICATION_JSON)
                .get();

        String retorno = response.readEntity(String.class);

        System.out.println("Response code: " + response.getStatus());
        System.out.println("Response message: " + retorno);

        List<Compartilhado> aux = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<Map<String, Object>>> map = mapper.readValue(retorno, new TypeReference<Map<String, List<Map<String, Object>>>>(){});
            List<Map<String, Object>> livros = map.get("itens");

            for (Map<String, Object> cada: livros) {
                aux.add(new Compartilhado(cada.get("titulo").toString(), cada.get("autor").toString()));
            }
            return aux;
        } catch (Exception e) {
            e.printStackTrace();

            //Caso a API falhe, popula uma lista padrao
            for (int i = 0; i < QTD_LIVROS; i++) {
                aux.add(new Compartilhado("LivroP " + i, "AutorP " + i));
            }
            return aux;
        }


    }

}
