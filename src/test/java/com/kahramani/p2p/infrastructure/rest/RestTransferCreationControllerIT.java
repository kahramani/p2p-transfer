package com.kahramani.p2p.infrastructure.rest;

import com.kahramani.p2p.AbstractRestIntegrationTest;
import com.kahramani.p2p.application.model.request.TransferCreationRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.restassured.http.ContentType;
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

public class RestTransferCreationControllerIT extends AbstractRestIntegrationTest {

    private static final int TEST_SERVER_PORT = 6438;
    private static AbstractVerticle verticle;

    @BeforeClass
    public static void setUpClass(TestContext context) throws SQLException {
        verticle = deploy(context, TEST_SERVER_PORT, "testRestTransferCreationControllerIT");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        tearDown(verticle);
    }

    @Test
    public void should_create_transfer() {
        String accountReference = "F62QHCMPLHFOHID";
        String path = String.format("/api/v1/accounts/%s/transfers", accountReference);
        TransferCreationRequest transferCreationRequest = new TransferCreationRequest();
        transferCreationRequest.setReceiverMobilePhone("+442033228352");
        transferCreationRequest.setAmount(new BigDecimal("382.8"));
        transferCreationRequest.setToCurrency("GBP");

        given().body(Json.encodePrettily(transferCreationRequest))
                .contentType(ContentType.JSON)
                .when()
                .post(path)
                .then()
                .statusCode(HttpResponseStatus.CREATED.code())
                .body("amount", equalTo("382.8"))
                .body("fromCurrency", equalTo("EUR"))
                .body("convertedAmount", equalTo("344.520"))
                .body("toCurrency", equalTo("GBP"))
                .body("exchangeRate", equalTo("0.90"))
                .body("transferReference", notNullValue())
                .body("receiverAccount", equalTo("CHQ7DAURKL1O8FB"))
                .body("senderAccount", equalTo("F62QHCMPLHFOHID"));
    }

    @Test
    public void should_return_http_404_when_request_body_is_not_a_json() {
        String accountReference = "X2QCL6BNB7ZL49K";
        String path = String.format("/api/v1/accounts/%s/transfers", accountReference);

        given().body("not-json")
                .contentType(ContentType.JSON)
                .when()
                .post(path)
                .then()
                .statusCode(HttpResponseStatus.BAD_REQUEST.code())
                .body("errorCode", equalTo(1500))
                .body("errorMessage", equalTo("Your request is invalid!"));
    }

    @Test
    public void should_return_http_400_when_currency_is_not_supported() {
        String accountReference = "X2QCL6BNB7ZL49K";
        String path = String.format("/api/v1/accounts/%s/transfers", accountReference);
        TransferCreationRequest transferCreationRequest = new TransferCreationRequest();
        transferCreationRequest.setReceiverMobilePhone("+442036950999");
        transferCreationRequest.setAmount(new BigDecimal("10.3"));
        transferCreationRequest.setToCurrency("USD");

        given().body(Json.encodePrettily(transferCreationRequest))
                .contentType(ContentType.JSON)
                .when()
                .post(path)
                .then()
                .statusCode(HttpResponseStatus.BAD_REQUEST.code())
                .body("errorCode", equalTo(1507))
                .body("errorMessage", equalTo("Currency is not supported!"));
    }

    @Test
    public void should_return_http_404_when_account_not_found() {
        String accountReference = "account-not-present";
        String path = String.format("/api/v1/accounts/%s/transfers", accountReference);
        TransferCreationRequest transferCreationRequest = new TransferCreationRequest();
        transferCreationRequest.setReceiverMobilePhone("+442036950999");
        transferCreationRequest.setAmount(new BigDecimal("10.3"));
        transferCreationRequest.setToCurrency("TRY");

        given().body(Json.encodePrettily(transferCreationRequest))
                .contentType(ContentType.JSON)
                .when()
                .post(path)
                .then()
                .statusCode(HttpResponseStatus.NOT_FOUND.code())
                .body("errorCode", equalTo(1550))
                .body("errorMessage", equalTo("Account not found!"));
    }

    @Test
    public void should_return_http_404_when_user_not_found() {
        String accountReference = "X2QCL6BNB7ZL49K";
        String path = String.format("/api/v1/accounts/%s/transfers", accountReference);
        TransferCreationRequest transferCreationRequest = new TransferCreationRequest();
        transferCreationRequest.setReceiverUsername("user-not-present");
        transferCreationRequest.setAmount(new BigDecimal("10.3"));
        transferCreationRequest.setToCurrency("TRY");

        given().body(Json.encodePrettily(transferCreationRequest))
                .contentType(ContentType.JSON)
                .when()
                .post(path)
                .then()
                .statusCode(HttpResponseStatus.NOT_FOUND.code())
                .body("errorCode", equalTo(1551))
                .body("errorMessage", equalTo("User not found!"));
    }

    @Test
    public void should_return_http_422_when_balance_is_insufficient() {
        String accountReference = "M5VHFXTZ8BC3TOO";
        String path = String.format("/api/v1/accounts/%s/transfers", accountReference);
        TransferCreationRequest transferCreationRequest = new TransferCreationRequest();
        transferCreationRequest.setReceiverMobilePhone("+442038720620");
        transferCreationRequest.setAmount(new BigDecimal("100"));
        transferCreationRequest.setToCurrency("EUR");

        given().body(Json.encodePrettily(transferCreationRequest))
                .contentType(ContentType.JSON)
                .when()
                .post(path)
                .then()
                .statusCode(HttpResponseStatus.UNPROCESSABLE_ENTITY.code())
                .body("errorCode", equalTo(1600))
                .body("errorMessage", equalTo("Balance is not sufficient to execute transfer!"));
    }

    @Test
    public void should_return_http_422_when_circular_transfer_tried() {
        String accountReference = "IV4EFMREFLIIIQO";
        String path = String.format("/api/v1/accounts/%s/transfers", accountReference);
        TransferCreationRequest transferCreationRequest = new TransferCreationRequest();
        transferCreationRequest.setReceiverMobilePhone("+442036950999");
        transferCreationRequest.setAmount(new BigDecimal("10.3"));
        transferCreationRequest.setToCurrency("GBP");

        given().body(Json.encodePrettily(transferCreationRequest))
                .contentType(ContentType.JSON)
                .when()
                .post(path)
                .then()
                .statusCode(HttpResponseStatus.UNPROCESSABLE_ENTITY.code())
                .body("errorCode", equalTo(1601))
                .body("errorMessage", equalTo("You cannot transfer money from account to the same account!"));
    }
}