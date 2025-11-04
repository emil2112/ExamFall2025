package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final CandidateRoutes candidateRoutes = new CandidateRoutes();
    private final SkillRoutes skillRoutes = new SkillRoutes();

    public EndpointGroup getRoutes(){
        return () -> {
            path("/skills", skillRoutes.getRoutes());
            path("/candidates", candidateRoutes.getRoutes());
        };
    }
}
