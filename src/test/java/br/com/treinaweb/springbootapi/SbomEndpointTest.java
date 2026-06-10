package br.com.treinaweb.springbootapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
class SbomEndpointTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void sbomEndpointShouldBeAvailable() throws Exception {
        mockMvc.perform(get("/actuator/sbom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ids").exists())
                .andExpect(jsonPath("$.ids[0]").value("application"));
    }

    @Test
    void sbomApplicationEndpointShouldBeAvailable() throws Exception {
        mockMvc.perform(get("/actuator/sbom/application"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bomFormat").value("CycloneDX"));
    }
}
