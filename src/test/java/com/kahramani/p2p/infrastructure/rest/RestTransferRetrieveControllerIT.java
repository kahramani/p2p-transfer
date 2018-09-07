package com.kahramani.p2p.infrastructure.rest;

import com.kahramani.p2p.AbstractRestIntegrationTest;
import com.kahramani.p2p.application.model.request.TransferCreationRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.TestContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RestTransferRetrieveControllerIT extends AbstractRestIntegrationTest {

    private static final int TEST_SERVER_PORT = 6439;
    private static AbstractVerticle verticle;

    @BeforeClass
    public static void setUpClass(TestContext context) throws SQLException {
        verticle = deploy(context, TEST_SERVER_PORT, "testRestTransferRetrieveControllerIT");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        tearDown(verticle);
    }

    @Test
    public void should_retrieve_created_transfer() {
        String accountReference = "F62QHCMPLHFOHID";
        String createPath = String.format("/api/v1/accounts/%s/transfers", accountReference);
        TransferCreationRequest transferCreationRequest = new TransferCreationRequest();
        transferCreationRequest.setReceiverMobilePhone("+442033228352");
        transferCreationRequest.setAmount(new BigDecimal("382.8"));
        transferCreationRequest.setToCurrency("GBP");

        Response response = given().body(Json.encodePrettily(transferCreationRequest))
                .contentType(ContentType.JSON)
                .when()
                .post(createPath);

        final String transferReference = response.getBody().jsonPath().getString("transferReference");
        String retrievePath = String.format("/api/v1/transfers/%s", transferReference);
        given().contentType(ContentType.JSON)
                .when()
                .get(retrievePath)
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body("transferReference", equalTo(transferReference))
                .body("date", notNullValue())
                .body("fromAmount", equalTo("382.80"))
                .body("fromCurrency", equalTo("EUR"))
                .body("toAmount", equalTo("344.52"))
                .body("toCurrency", equalTo("GBP"))
                .body("exchangeRate", equalTo("0.90"))
                .body("receiverAccount", equalTo("CHQ7DAURKL1O8FB"))
                .body("senderAccount", equalTo("F62QHCMPLHFOHID"));
    }

    @Test
    public void should_return_http_404_when_transfer_not_found() {
        String transferReference = "transfer-not-present";
        String retrievePath = String.format("/api/v1/transfers/%s", transferReference);

        given().contentType(ContentType.JSON)
                .when()
                .get(retrievePath)
                .then()
                .statusCode(HttpResponseStatus.NOT_FOUND.code())
                .body("errorCode", equalTo(1552))
                .body("errorMessage", equalTo("Transfer not found!"));
    }
}