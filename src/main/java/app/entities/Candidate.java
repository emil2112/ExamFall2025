package app.entities;

import app.dtos.CandidateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private String name;

    private String phone;

    private String educationBackground;

    //TODO: overvej cascadeType
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "candidate_skill",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    public Candidate(CandidateDTO candidateDTO){
        this.id = candidateDTO.getId();
        this.name = candidateDTO.getName();
        this.phone = candidateDTO.getPhone();
        this.educationBackground = candidateDTO.getEducationBackground();
    }

    public void addSkill(Skill skill){
        skills.add(skill);
    }
}
