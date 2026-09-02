package br.com.andrewdesigner.web;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class PortfolioResourceTest {
    @Test
    void paginaPrincipalCarrega() {
        given().when().get("/").then()
                .statusCode(200)
                .contentType("text/html")
                .body(containsString("ANDREW MAURICIO"))
                .body(containsString("O que eu construo"));
    }
}
