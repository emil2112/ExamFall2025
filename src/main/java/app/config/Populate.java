package app.config;

import app.dtos.CandidateDTO;
import app.dtos.SkillDTO;
import app.entities.Candidate;
import app.entities.Skill;
import app.enums.Category;
import app.exceptions.DAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.util.List;

public class Populate {
    public static void populate(){
        EntityManagerFactory emf= HibernateConfig.getEntityManagerFactory();

        List<SkillDTO> skillDTOList = List.of(
                new SkillDTO("Java", Category.PROG_LANG, "Object oriented programming language"),
                new SkillDTO("PostgreSQL", Category.DB, "Database language"),
                new SkillDTO("Docker", Category.DEVOPS, "Docker skill"),
                new SkillDTO("JUnit", Category.TESTING, "Unit testing skills")
        );

        List<CandidateDTO> candidateDTOList = List.of(
                new CandidateDTO("Lars", "32850679", "Software engineer"),
                new CandidateDTO("Bo", "12345678", "Computer scientist")
        );

        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();

            for(SkillDTO s : skillDTOList){
                Skill skill = new Skill(s);
                em.persist(skill);
            }

            for(CandidateDTO s : candidateDTOList){
                Candidate candidate = new Candidate(s);
                em.persist(candidate);
            }

            em.getTransaction().commit();
            System.out.println("Database populated!");
        } catch (DAOException e) {
            System.out.println("Error while populating database " + e.getMessage());
        }

    }
}
