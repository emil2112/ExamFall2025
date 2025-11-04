package app.controllers;

import app.config.HibernateConfig;
import app.daos.CandidateDAO;
import app.dtos.CandidateDTO;
import app.entities.Candidate;
import app.enums.Category;
import app.exceptions.ApiException;
import app.exceptions.DAOException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CandidateController {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final CandidateDAO candidateDAO = CandidateDAO.getInstance(emf);

    public void getAllCandidates(Context ctx){
        String category = ctx.queryParam("category");
        List<CandidateDTO> candidateDTOList = candidateDAO.getAll();
        Set<String> allowed = Set.of("PROG_LANG", "DB", "DEVOPS", "TESTING");

        if(category == null || category.isBlank()){
            ctx.status(200).json(candidateDTOList);
            return;
        }

        if(!allowed.contains(category)){
            ctx.status(400).result("Category not found");
        }else{
            //String category is converted to an Enum to be able to compare to the category variable on a Skill object
            Category categoryEnum = Category.valueOf(category.toUpperCase());
            List<CandidateDTO> candidatesByCategory = candidateDTOList.stream()
                    .filter(candidate -> candidate.getSkills() != null && candidate.getSkills().stream()
                            .anyMatch(skill -> skill.getCategory().equals(categoryEnum))).toList();

            ctx.status(200).json(candidatesByCategory);
        }

    }

    public void getCandidateById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        CandidateDTO candidateDTO = candidateDAO.getById(id);
        ctx.status(200).json(candidateDTO);
    }

    public void createCandidate(Context ctx){
        CandidateDTO candidateDTO = ctx.bodyAsClass(CandidateDTO.class);
        CandidateDTO trip = candidateDAO.create(candidateDTO);
        ctx.status(HttpStatus.CREATED).json(trip);
        ctx.res().setStatus(201);
    }

    public void updateCandidate(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        CandidateDTO candidateDTO = ctx.bodyAsClass(CandidateDTO.class);
        CandidateDTO candidate = candidateDAO.getById(id);
        if(candidate == null){
            ctx.status(HttpStatus.NOT_FOUND).result("Guide Not Found");
        }

        CandidateDTO updatedCandidate = candidateDAO.update(candidateDTO);
        ctx.json(updatedCandidate);
        ctx.res().setStatus(200);
    }

    public void deleteCandidate(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        CandidateDTO candidateDTO = candidateDAO.getById(id);
        if(candidateDTO == null){
            ctx.status(HttpStatus.NOT_FOUND).result("Guide Not Found");
        }

        Boolean tripDeleted = candidateDAO.delete(id);
        ctx.status(HttpStatus.OK).result("Entity deleted");
    }

    public void addSkillToCandidate(Context ctx){
        int candidateId = Integer.parseInt(ctx.pathParam("candidateId"));
        int skillId = Integer.parseInt(ctx.pathParam("skillId"));

        try {
            CandidateDTO candidate = candidateDAO.addSkill(candidateId, skillId);

            if(candidate == null){
                ctx.status(HttpStatus.NOT_FOUND).result("Failed to add skill to guide");
            }

            ctx.status(HttpStatus.OK).json(candidate);
        } catch (DAOException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .result("Error adding skill to the candidate in the database " + e.getMessage());
        } catch (Exception e) {
            throw new ApiException(500, "Unexpected error while adding skill to the candidate");
        }
    }
}
