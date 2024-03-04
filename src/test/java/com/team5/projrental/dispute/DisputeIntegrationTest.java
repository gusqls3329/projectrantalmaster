package com.team5.projrental.dispute;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.dispute.model.DisputeDto;
import com.team5.projrental.entities.User;
import com.team5.projrental.user.UserController;
import com.team5.projrental.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WithMockUser(username = "test", roles = "USER")
public class DisputeIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @Autowired
    UserRepository userRepository;

    String tokenKey = "Authorization";
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoie1wiaXVzZXJcIjoxLFwiYXV0aFwiOlwiVVNFUlwifSIsImlhdCI6MTcwODc3MzM2OCwiZXhwIjoxMDAwMDAxNzA4NzczMzY4fQ.iFR36yY9NHsXJmfy1POKW-Zz2ok0hG-dQvZ3bGAKC_g";

    @Autowired
    UserController userController;


    @Test
    void postDisputeProduct() throws Exception {

        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dispute/product")
                        .header(tokenKey, token)
                        .contentType("application/json")
                        .content(om.writeValueAsString(disputeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));

    }

    @Test
    void postDisputeUser() {


    }


}
