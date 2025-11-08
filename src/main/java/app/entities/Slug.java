package app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Slug {

    private int id;
    private String slug;
    private String name;
    private String categoryKey;
    private String description;
    private int popularityScore;
    private int averageSalary;
}
