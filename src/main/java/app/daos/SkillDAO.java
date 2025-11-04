package app.daos;

import app.config.ApplicationConfig;
import app.dtos.CandidateDTO;
import app.dtos.SkillDTO;
import app.entities.Candidate;
import app.entities.Skill;
import app.exceptions.DAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SkillDAO implements IDAO <SkillDTO, Integer>{

    private static SkillDAO instance;
    private static EntityManagerFactory emf;
    private static Logger logger = LoggerFactory.getLogger(SkillDAO.class);

    public static SkillDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new SkillDAO();
            SkillDAO.emf = emf;
        }
        return instance;
    }

    @Override
    public SkillDTO getById(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Skill skill = em.find(Skill.class, integer);
            return new SkillDTO(skill);
        } catch (Exception e) {
            throw new DAOException("Error while fetching Skill from database", e);
        }
    }

    @Override
    public List<SkillDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<SkillDTO> query = em.createQuery("SELECT new app.dtos.SkillDTO(t) FROM Skill t", SkillDTO.class);
            return query.getResultList();
        }catch (Exception e) {
            throw new DAOException("Error while fetching Skills from database", e);
        }
    }

    @Override
    public SkillDTO create(SkillDTO skillDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill skill = new Skill(skillDTO);
            em.persist(skill);
            em.getTransaction().commit();
            return new SkillDTO(skill);
        }catch (Exception e) {
            throw new DAOException("Error while creating new Skill in the database", e);
        }
    }

    @Override
    public SkillDTO update(SkillDTO skillDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill skill = new Skill(skillDTO);
            em.merge(skill);
            em.getTransaction().commit();
            return new SkillDTO(skill);
        }catch (Exception e) {
            throw new DAOException("Error while updating Skill in the database", e);
        }
    }

    @Override
    public boolean delete(Integer integer) {
        EntityManager em = emf.createEntityManager();

        try {
            Skill skill = em.find(Skill.class, integer);

            em.getTransaction().begin();
            em.remove(skill);
            em.getTransaction().commit();
            logger.info("Skill with ID: " + skill.getId() + " has been deleted");
            return true;
        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.info("Something went wrong. Changes have been rolled back");
            throw new DAOException("Error while deleting skill from database", e);
        }
    }
}
