package app.entities;

import app.dtos.SkillDTO;
import app.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
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

    @ManyToMany(mappedBy = "skills", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Candidate> candidates = new HashSet<>();

    public Skill(SkillDTO skillDTO){
        this.id = skillDTO.getId();
        this.category = skillDTO.getCategory();
        this.name = skillDTO.getName();
        this.description = skillDTO.getDescription();
    }
}
