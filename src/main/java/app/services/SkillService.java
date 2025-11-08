package app.services;

import app.entities.Slug;
import app.entities.SlugResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class SkillService {

    ObjectMapper objectMapper = new ObjectMapper();

    HttpClient httpClient = HttpClient.newHttpClient();


    public Slug getSlug (String skillName){

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats?slugs=" + skillName))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                String json = response.body();
                SlugResponse slugResponse = objectMapper.readValue(json, SlugResponse.class);

                if(slugResponse != null){
                    Slug[] slug = slugResponse.getData().toArray(new Slug[0]);
                    return slug[0];
                }
            }else{
                System.out.println("Error reading enrichment data API" + response.statusCode());
                return null;
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new Slug();
    }
}
