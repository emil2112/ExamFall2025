package app.routes;


import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.CandidateDAO;
import app.daos.IDAO;
import app.dtos.CandidateDTO;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import populators.Populator;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class CandidateRouteTest {
    private static Javalin app; // only non-null if we start the server here
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static IDAO dao;
    private Populator populator = new Populator(emf);
    private static String securityToken;

    @BeforeAll
    static void init() {

        app = ApplicationConfig.startServer(7070);
        dao = CandidateDAO.getInstance(emf);

        RestAssured.baseURI = "http://localhost:7070";
        RestAssured.basePath = "/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.createNativeQuery("TRUNCATE TABLE candidate, skill " +
                    "RESTART IDENTITY CASCADE").executeUpdate();
            em.getTransaction().commit();
            populator.populateUsers();
            populator.populateEntities();
            securityToken = given()
                    .contentType("application/json")
                    .body("{\"username\":\"admin\",\"password\":\"admin\"}")
                    .when()
                    .post("/auth/login")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .extract()
                    .path("token");

            System.out.println(securityToken);
        }
    }

    @AfterAll
    static void closeDown() {
        if (app != null) {
            ApplicationConfig.stopServer(app);
        }
    }

    @Test
    void testGetCandidateById() {
        given()
                .header("Authorization", "Bearer " + securityToken)
                .pathParams("id", 1)
                .when()
                .get("/candidates/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Lars"));
    }

    @Test
    void testGetCandidates() {
        int listSize = 2;

        CandidateDTO[] skillDTOS =
                given()
                        .header("Authorization", "Bearer " + securityToken)
                        .when()
                        .get("/candidates")
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract()
                        .as(CandidateDTO[].class);

        assertThat("Expected 2 Trips", skillDTOS.length, equalTo(listSize));
    }

    @Test
    void testCreateCandidate() {
        CandidateDTO candidateDTO = new CandidateDTO(
                "Bo",
                "12345678",
                "Computer scientist"
        );

        CandidateDTO newCandidateDTO =
                given()
                        .header("Authorization", "Bearer " + securityToken)
                        .contentType("application/json")
                        .body(candidateDTO)
                        .when()
                        .post("/candidates")
                        .then()
                        .assertThat()
                        .statusCode(201)
                        .extract()
                        .as(CandidateDTO.class);

        assertEquals(candidateDTO.getName(), newCandidateDTO.getName());
        assertEquals(candidateDTO.getPhone(), newCandidateDTO.getPhone());
        assertEquals(candidateDTO.getEducationBackground(), newCandidateDTO.getEducationBackground());
    }

    @Test
    void testUpdateCandidate() {
        CandidateDTO updatedCandidateDTO = new CandidateDTO(
                "Mogens",
                "12345678",
                "Computer scientist"
        );

        given()
                .header("Authorization", "Bearer " + securityToken)
                .contentType("application/json")
                .body(updatedCandidateDTO)
                .pathParam("id", 1)
                .when()
                .put("/candidates/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("Mogens"));
    }

    @Test
    void testDeleteCandidate(){
        given()
                .header("Authorization", "Bearer " + securityToken)
                .pathParam("id", 1)
                .when()
                .delete("/candidates/{id}")
                .then()
                .statusCode(200)
                .body(equalTo("Entity deleted"));
    }


}
