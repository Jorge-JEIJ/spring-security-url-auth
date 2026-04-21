package com.example.demo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class VipControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void securedEndpoint_rejectsAnonymousUser() throws Exception {
        mockMvc.perform(get("/vip/")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    void securedEndpoint_rejectsNonVipUser() throws Exception {
        mockMvc.perform(get("/vip/")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "1", password = "password", roles = "VIP")
    void VipUserCanAccessAllEndpoints() throws Exception {
        mockMvc.perform(get("/vip/")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "1", password = "password",  roles = "VIP")
    void welcomeMessageIncludesUsername() throws Exception {
        mockMvc.perform(get("/vip/welcome"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }
}
