package app.entities;

import app.dtos.SkillDTO;
import app.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String description;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private Set<Candidate> candidates = new HashSet<>();

    public Skill(SkillDTO skillDTO){
        this.id = skillDTO.getId();
        this.category = skillDTO.getCategory();
        this.name = skillDTO.getName();
        this.description = skillDTO.getDescription();
    }
}
