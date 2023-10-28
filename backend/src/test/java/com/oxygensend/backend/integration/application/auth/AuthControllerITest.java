package com.oxygensend.backend.integration.application.auth;

import com.oxygensend.backend.integration.BaseMvcITest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Sql(scripts = {"classpath:data/user.sql", "classpath:data/session.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:data/clear_db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthControllerITest extends BaseMvcITest {


    @Test
    public void testRegister_Successful() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content("{" +
                                                               "\"firstName\":\"John\"," +
                                                               "\"lastName\":\"Doe\"," +
                                                               "\"email\":\"test@example.com\"," +
                                                               "\"password\":\"password\"" +
                                                               "}"
                                              )
               )
               .andExpect(MockMvcResultMatchers.status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
               .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists());

    }

    @Test
    public void testRegister_BadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content("{" +
                                                               "\"firstName\":\"John\"," +
                                                               "\"email\":\"testcom\"" +
                                                               "}"
                                              )
               )
               .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testAuthenticate_Successful() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/access_token")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content("{" +
                                                               "\"email\":\"test@test.com\"," +
                                                               "\"password\":\"test\"}"
                                              )
               )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
               .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists())
               .andDo(print());

    }

    @Test
    public void testAuthenticate_BadCredentials() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/access_token")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content("{" +
                                                               "\"email\":\"test@test.com\"," +
                                                               "\"password\":\"password\"}"
                                              )
               )
               .andExpect(MockMvcResultMatchers.status().isUnauthorized())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
               .andDo(print());

    }

    @Test
    public void testRefreshToken_Successful() throws Exception {


        var accessToken = login();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh_token")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(String.format("{\"token\": \"%s\"}", accessToken)
                                              )
               )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
               .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists())
               .andDo(print());
    }

}
