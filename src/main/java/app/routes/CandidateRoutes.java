package app.routes;

import app.controllers.CandidateController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class CandidateRoutes {

    private final CandidateController controller = new CandidateController();

    protected EndpointGroup getRoutes(){

        return () -> {
            get("/", controller::getAllCandidates, Role.ADMIN);
            get("/{id}", controller::getCandidateById, Role.ADMIN);
            post("/", controller::createCandidate, Role.ADMIN);
            put("/{id}", controller::updateCandidate, Role.ADMIN);
            delete("/{id}", controller::deleteCandidate, Role.ADMIN);
            put("/{candidateId}/skills/{skillId}", controller::addSkillToCandidate, Role.ADMIN);
        };
    }

}
