package org.zalando.apidiscovery.crawler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class YamlToJsonConverterTest {

    @Test
    public void testYamlToJsonConvert() throws IOException {
        String yaml = "swagger: '2.0'\n" +
                "info:\n" +
                "  title: My API\n" +
                "  description: Some description\n" +
                "  version: \"0.0.2\"";

        JsonNode expectedJson = new ObjectMapper().createObjectNode()
                .put("swagger", "2.0")
                .set("info", new ObjectMapper().createObjectNode()
                        .put("title", "My API")
                        .put("description", "Some description")
                        .put("version", "0.0.2"));

        assertThat(new ObjectMapper(new YAMLFactory()).readValue(yaml, JsonNode.class))
                .isEqualTo(expectedJson);
    }
}
