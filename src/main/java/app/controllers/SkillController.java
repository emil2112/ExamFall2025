package app.controllers;

import app.config.HibernateConfig;
import app.daos.SkillDAO;
import app.dtos.SkillDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SkillController {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final SkillDAO skillDAO = SkillDAO.getInstance(emf);

    public void getAllSkills(Context ctx){
        List<SkillDTO> tripList = skillDAO.getAll();
        ctx.json(tripList);
    }

    public void getSkillById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        SkillDTO skillDTO = skillDAO.getById(id);
        ctx.json(skillDTO);
    }

    public void createSkill(Context ctx){
        SkillDTO skillDTO = ctx.bodyAsClass(SkillDTO.class);
        SkillDTO trip = skillDAO.create(skillDTO);
        ctx.status(HttpStatus.CREATED).json(trip);
        ctx.res().setStatus(201);
    }

    public void updateSkill(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        SkillDTO candidateDTO = ctx.bodyAsClass(SkillDTO.class);
        SkillDTO candidate = skillDAO.getById(id);
        if(candidate == null){
            ctx.status(HttpStatus.NOT_FOUND).result("Guide Not Found");
        }

        SkillDTO updatedCandidate = skillDAO.update(candidateDTO);
        ctx.json(updatedCandidate);
        ctx.res().setStatus(200);
    }

    public void deleteSkill(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        SkillDTO candidateDTO = skillDAO.getById(id);
        if(candidateDTO == null){
            ctx.status(HttpStatus.NOT_FOUND).result("Guide Not Found");
        }

        Boolean tripDeleted = skillDAO.delete(id);
        ctx.status(HttpStatus.OK).result("Entity deleted");
    }
}
