package org.arachna.jsonexporter.rest;

import java.io.IOException;

import org.arachna.jsonexporter.AbstractBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JsonExportResourceTest extends AbstractBaseTest {
    @ParameterizedTest
    @CsvSource(value = { "default,/simpleValue.json,/simpleValue.metric",
        "nested_structured_as_several_metrics_using_value,/structuredHealth.json,/structuredHealth.metric",
        "nested_structured_as_several_metrics_using_object,/structuredHealth.json,/structuredHealth1.metric" })
    public void testModuleHandlers(String moduleName, String jsonResource, String expectedResultResource) throws IOException {
        final String context = String.format("/probe?module=%s&target=http://localhost%s", moduleName, jsonResource);
        given().when().get(context).then().statusCode(200).body(is(readClasspathRessource(expectedResultResource)));
    }
}
