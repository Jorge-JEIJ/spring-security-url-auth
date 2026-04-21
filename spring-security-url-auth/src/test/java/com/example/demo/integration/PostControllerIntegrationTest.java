package com.example.demo.integration;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /* ---------- PUBLIC ---------- */

    @Test
    void publicEndpoint_isAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/posts/public"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("PUBLIC")));
    }

    /* ---------- AUTHENTICATION ---------- */

    @Test
    void securedEndpoint_rejectsAnonymousUser() throws Exception {
        mockMvc.perform(get("/posts/1")).andExpect(status().isUnauthorized());
    }

    /* ---------- USER FLOW ---------- */

    @Test
    void user_canReadAndCreatePosts_butCannotDelete() throws Exception {
        // GET allowed
        mockMvc.perform(get("/posts/1").with(httpBasic("user", "password"))).andExpect(status().isOk());

        // POST allowed
        mockMvc.perform(post("/posts").with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("created")));

        // DELETE forbidden
        mockMvc.perform(delete("/posts/1").with(httpBasic("user", "password"))).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "1" , password = "password", roles = "USER") //defaults are already "user" and "password" lol
    void mockUser_canReadAndCreatePosts_butCannotDelete() throws Exception {
        // GET allowed
        mockMvc.perform(get("/posts/1")).andExpect(status().isOk());

        // POST allowed
        mockMvc.perform(post("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("created")));

        // DELETE forbidden
        mockMvc.perform(delete("/posts/1")).andExpect(status().isForbidden());
    }

    /* ---------- ADMIN FLOW ---------- */

    @Test
    void admin_canReadAndDelete_butCannotCreate() throws Exception {
        // GET allowed
        mockMvc.perform(get("/posts/1").with(httpBasic("admin", "password"))).andExpect(status().isOk());

        // POST forbidden
        mockMvc.perform(post("/posts").with(httpBasic("admin", "password"))).andExpect(status().isForbidden());

        // DELETE allowed
        mockMvc.perform(delete("/posts/1").with(httpBasic("admin", "password")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted")));
    }

    @Test
    @WithMockUser(username = "admin" , password = "password", roles = "ADMIN")
    void mockAdmin_canReadAndDelete_butCannotCreate() throws Exception {
        // GET allowed
        mockMvc.perform(get("/posts/1")).andExpect(status().isOk());

        // POST Forbidden
        mockMvc.perform(post("/posts")).andExpect(status().isForbidden());

        // DELETE Allowed
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted")));
    }

    /* ---------- INVALID CREDENTIALS ---------- */

    @Test
    void invalidCredentials_areRejected() throws Exception {
        mockMvc.perform(get("/posts/1").with(httpBasic("user", "wrongpassword")))
                .andExpect(status().isUnauthorized());
    }
}
