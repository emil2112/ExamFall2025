package app.dtos;

import app.entities.Skill;
import app.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillDTO {

    int id;

    private String name;

    private Category category;

    private String description;

    public SkillDTO (Skill skill){
        this.id = skill.getId();
        this.name = skill.getName();
        this.category = skill.getCategory();
        this.description = skill.getDescription();
    }

    public SkillDTO(String name, Category category, String description){
        this.name = name;
        this.category = category;
        this.description = description;
    }
}
