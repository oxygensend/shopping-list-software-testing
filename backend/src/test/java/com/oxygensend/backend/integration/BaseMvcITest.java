package com.oxygensend.backend.integration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.oxygensend.backend.config.TokenConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@WebAppConfiguration(value = "")
public class BaseMvcITest extends BaseITest {

    @Autowired
    protected WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.objectMapper = new JsonMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply(springSecurity()).build();
    }

    public String login() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/access_token")
                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                    .content("{" +
                                                                                     "\"email\":\"test@test.com\"," +
                                                                                     "\"password\":\"test\"}"
                                                                    )
                                     )
                                     .andExpect(MockMvcResultMatchers.status().isOk())
                                     .andReturn();

        var content = mvcResult.getResponse().getContentAsString();
        var objectMapper = new ObjectMapper();
        return objectMapper.readTree(content).get("refreshToken").asText();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TokenConfiguration tokenConfiguration() {
            TokenConfiguration config = new TokenConfiguration();
            config.secretKey = "614E645267556B58703273357638792F413F4428472B4B6250655368566D597133743677397A244326452948404D635166546A576E5A7234753778214125442A";
            config.authExpirationMs = 3600000;
            config.refreshExpirationMs = 86400000;
            return config;
        }
    }
}
