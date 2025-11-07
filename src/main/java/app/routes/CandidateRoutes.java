package app.routes;

import app.controllers.CandidateController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class CandidateRoutes {

    private final CandidateController controller = new CandidateController();

    protected EndpointGroup getRoutes(){

        return () -> {
            get("/", controller::getAllCandidates, Role.USER);
            get("/{id}", controller::getCandidateById, Role.USER);
            post("/", controller::createCandidate, Role.USER);
            put("/{id}", controller::updateCandidate, Role.USER);
            delete("/{id}", controller::deleteCandidate, Role.USER);
            put("/{candidateId}/skills/{skillId}", controller::addSkillToCandidate, Role.USER);
        };
    }

}
