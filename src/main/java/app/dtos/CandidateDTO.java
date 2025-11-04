package app.dtos;

import app.entities.Candidate;
import app.entities.Skill;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidateDTO {

    private int id;

    private String name;

    private String phone;

    private String educationBackground;

    @JsonIgnoreProperties("candidate")
    private Set<Skill> skills = new HashSet<>();

    public CandidateDTO(Candidate candidate){
        this.id = candidate.getId();
        this.name = candidate.getName();
        this.phone = candidate.getPhone();
        this.educationBackground = candidate.getEducationBackground();
        this.skills = candidate.getSkills();

    }

    public CandidateDTO(String name, String phone, String educationBackground){
        this.name = name;
        this.phone = phone;
        this.educationBackground = educationBackground;
    }
}
