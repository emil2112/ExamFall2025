package app.daos;

import app.config.ApplicationConfig;
import app.dtos.CandidateDTO;
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

public class CandidateDAO implements IDAO <CandidateDTO, Integer>{

    private static CandidateDAO instance;
    private static EntityManagerFactory emf;
    private static Logger logger = LoggerFactory.getLogger(CandidateDAO.class);

    public static CandidateDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new CandidateDAO();
            CandidateDAO.emf = emf;
        }
        return instance;
    }

    @Override
    public CandidateDTO getById(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Candidate candidate = em.find(Candidate.class, integer);
            return new CandidateDTO(candidate);
        } catch (Exception e) {
            throw new DAOException("Error when finding Candidate in database", e);
        }
    }

    @Override
    public List<CandidateDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<CandidateDTO> query = em.createQuery("SELECT new app.dtos.CandidateDTO(t) FROM Candidate t", CandidateDTO.class);
            return query.getResultList();
        }catch (Exception e) {
            throw new DAOException("Error while fetching Candidates from database", e);
        }
    }

    @Override
    public CandidateDTO create(CandidateDTO candidateDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate candidate = new Candidate(candidateDTO);
            em.persist(candidate);
            em.getTransaction().commit();
            return new CandidateDTO(candidate);
        } catch (Exception e) {
            throw new DAOException("Error while creating Candidate in database", e);
        }
    }

    @Override
    public CandidateDTO update(CandidateDTO candidateDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate candidate = new Candidate(candidateDTO);
            em.merge(candidate);
            em.getTransaction().commit();
            return new CandidateDTO(candidate);
        }catch (Exception e) {
            throw new DAOException("Error while updating Candidate in database", e);
        }
    }

    @Override
    public boolean delete(Integer integer) {
        EntityManager em = emf.createEntityManager();

        try {
            Candidate candidate = em.find(Candidate.class, integer);

            em.getTransaction().begin();
            em.remove(candidate);
            em.getTransaction().commit();
            logger.info("Candidate with ID: " + candidate.getId() + " has been deleted");
            return true;
        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.info("Something went wrong. Changes have been rolled back");
            throw new DAOException("Error while deleting candidate from database", e);
        }
    }

    public CandidateDTO addSkill(Integer candidateId, Integer skillId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Candidate candidate = em.find(Candidate.class, candidateId);
            Skill skill = em.find(Skill.class, skillId);

            if (candidate == null || skill == null) {
                throw new DAOException("Candidate or Skill not found", null);
            }

            candidate.addSkill(skill);
            em.getTransaction().commit();

            candidate.getSkills().size();

            return new CandidateDTO(candidate);
        } catch (Exception e) {
            throw new DAOException("Error while adding skill to candidate in the database", e);
        }
    }
}
