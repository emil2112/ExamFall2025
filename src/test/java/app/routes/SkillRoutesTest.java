package app.routes;


import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.SkillDAO;
import app.daos.IDAO;
import app.dtos.SkillDTO;
import app.enums.Category;
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

public class SkillRoutesTest {
    private static Javalin app; // only non-null if we start the server here
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static IDAO dao;
    private Populator populator = new Populator(emf);
    private static String securityToken;

    @BeforeAll
    static void init() {

        // 2) Start server (this should read the same test config)
        app = ApplicationConfig.startServer(7070);
        dao = SkillDAO.getInstance(emf);

        // 4) Rest Assured base URL/URI
        RestAssured.baseURI = "http://localhost:7070";
        RestAssured.basePath = "/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // auto-log failures
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
                    .body("{\"username\":\"admin\",\"password\":\"admin\"}") // use a test user that exists
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
    void testGetSkillById() {
        given()
                .header("Authorization", "Bearer " + securityToken)
                .pathParams("id", 1)
                .when()
                .get("/skills/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Java"));
    }

    @Test
    void testGetSkills() {
        int listSize = 4;

        SkillDTO[] skillDTOS =
                given()
                        .header("Authorization", "Bearer " + securityToken)
                        .when()
                        .get("/skills")
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract()
                        .as(SkillDTO[].class);

        assertThat("Expected 2 Trips", skillDTOS.length, equalTo(listSize));
    }

    @Test
    void testCreateSkill() {
        SkillDTO skillDTO = new SkillDTO(
                "Java", Category.PROG_LANG, "Object oriented programming language"
        );

        SkillDTO newSkillDTO =
                given()
                        .header("Authorization", "Bearer " + securityToken)
                        .contentType("application/json")
                        .body(skillDTO)
                        .when()
                        .post("/skills")
                        .then()
                        .assertThat()
                        .statusCode(201)
                        .extract()
                        .as(SkillDTO.class);

        assertEquals(skillDTO.getName(), newSkillDTO.getName());
        assertEquals(skillDTO.getDescription(), newSkillDTO.getDescription());
    }

    @Test
    void testUpdateSkill() {
        SkillDTO updatedSkillDTO = new SkillDTO(
                "Java", Category.PROG_LANG, "OOP"
        );

        given()
                .header("Authorization", "Bearer " + securityToken)
                .contentType("application/json")
                .body(updatedSkillDTO)
                .pathParam("id", 1)
                .when()
                .put("/skills/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("description", equalTo("OOP"));
    }

    @Test
    void testDeleteSkill(){
        given()
                .header("Authorization", "Bearer " + securityToken)
                .pathParam("id", 1)
                .when()
                .delete("/skills/{id}")
                .then()
                .statusCode(200)
                .body(equalTo("Entity deleted"));
    }


}
