package com.marketplacehub.controller;

import com.marketplacehub.model.App;
import com.marketplacehub.service.MarketplaceService;
import com.microsaas.marketplacehub.MarketplaceHubApplication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@WebMvcTest(controllers = MarketplaceController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class}, properties = {
        "crosscutting.security.enabled=false",
        "crosscutting.tenancy.enabled=false"
})
@ContextConfiguration(classes = MarketplaceHubApplication.class)
public class MarketplaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarketplaceService marketplaceService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void testGetApps() throws Exception {
        App app = new App();
        app.setName("WealthEdge");
        Mockito.when(marketplaceService.getApps(null, null)).thenReturn(List.of(app));

        mockMvc.perform(get("/api/v1/apps")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("WealthEdge"));
    }
}
