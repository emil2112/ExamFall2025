package populators;

import app.config.HibernateConfig;
import app.daos.CandidateDAO;
import app.daos.SkillDAO;
import app.dtos.CandidateDTO;
import app.dtos.SkillDTO;
import app.entities.Candidate;
import app.entities.Skill;
import app.enums.Category;
import app.exceptions.DAOException;
import app.security.daos.SecurityDAO;
import app.security.entities.Role;
import app.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class Populator {

    private static CandidateDAO guideDAO;
    private static SkillDAO tripDAO;
    private static SecurityDAO securityDAO;
    private static EntityManagerFactory emf;

    public Populator(EntityManagerFactory emf) {
        this.emf = emf;
        this.guideDAO = new CandidateDAO();
        this.tripDAO = new SkillDAO();
        this.securityDAO = new SecurityDAO(emf);
    }

    public void populateUsers() {

        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Role adminRole = em.find(Role.class, "ADMIN");
            if(adminRole == null){
                adminRole = new  Role("ADMIN");
                em.persist(adminRole);
            }
            User user = em.find(User.class, "admin");
            if(user == null) {
                user = new User("admin","admin");
            }
            user.addRole(adminRole);
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new DAOException("Error adding User or Role from populator", e);
        }

    }

    public void cleanUpDb() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Candidate").executeUpdate();
            em.createQuery("DELETE FROM Skill").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateEntities (){

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
