package app.routes;

import app.controllers.SkillController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class SkillRoutes {

    private final SkillController controller = new SkillController();

    protected EndpointGroup getRoutes(){

        return () -> {
            get("/", controller::getAllSkills, Role.USER);
            get("/{id}", controller::getSkillById, Role.USER);
            post("/", controller::createSkill, Role.USER);
            put("/{id}", controller::updateSkill, Role.USER);
            delete("/{id}", controller::deleteSkill, Role.USER);
        };
    }

}
