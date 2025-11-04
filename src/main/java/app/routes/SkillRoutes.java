package app.routes;

import app.controllers.SkillController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class SkillRoutes {

    private final SkillController controller = new SkillController();

    protected EndpointGroup getRoutes(){

        return () -> {
            get("/", controller::getAllSkills, Role.ADMIN);
            get("/{id}", controller::getSkillById, Role.ADMIN);
            post("/", controller::createSkill, Role.ADMIN);
            put("/{id}", controller::updateSkill, Role.ADMIN);
            delete("/{id}", controller::deleteSkill, Role.ADMIN);
        };
    }

}
