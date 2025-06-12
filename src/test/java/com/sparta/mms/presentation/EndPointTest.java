package com.sparta.mms.presentation;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mms.application.UserService;
import com.sparta.mms.common.dto.response.Success;
import com.sparta.mms.common.exception.ErrorCode;
import com.sparta.mms.infrastructure.security.TokenProvider;
import com.sparta.mms.infrastructure.security.TokenStatus;
import com.sparta.mms.infrastructure.security.dto.request.LoginRequest;
import com.sparta.mms.presentation.dto.request.SignUpUserRequest;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EndPointTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    TokenProvider tokenProvider;

    String adminToken;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    void setUp() throws Exception {
        generateAdminToken();
        setUpUser();
    }


    @Test
    void 권한_변경_이후_기존_액세스_토큰_차단_테스트() throws Exception {
        // given
        long userId = 2L;
        LoginRequest loginRequest = LoginRequest.builder()
            .username("JIN HO")
            .password("1234")
            .build();

        String response = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/sign-in")
                    .content(objectMapper.writeValueAsString(loginRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andReturn()
            .getResponse()
            .getContentAsString();

        Success success = objectMapper.readValue(response, Success.class);

        Map<String, String> data = (Map<String, String>) success.data();
        String accessToken = data.get("accessToken");

        // when - admin 권한으로 유저 승격
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/admin/users/{userId}/roles", userId)
                .header("Authorization", "Bearer " + adminToken)
        );

        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.patch("/admin/users/{userId}/roles", userId)
                .header("Authorization", "Bearer " + accessToken)
        );

        // then - 승격 이전의 토큰 차단 확인
        result.andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ErrorCode.INVALID_TOKEN.getCode()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.INVALID_TOKEN.getMessage()));

    }

    @Test
    void 권한_변경_성공_테스트() throws Exception {
        // given
        long userId = 2L;

        // when
        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.patch("/admin/users/{userId}/roles", userId)
                .header("Authorization", "Bearer " + adminToken)
        );

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isAccepted())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("JIN HO"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value("Mentos"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value("ADMIN"))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void 권한_변경_실패_테스트_유저_없음() throws Exception {
        // given
        long userId = 999L;

        // when
        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.patch("/admin/users/{userId}/roles", userId)
                .header("Authorization", "Bearer " + adminToken)
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("USER_NOT_FOUND"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                .value(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @Test
    void 로그인_성공_테스트() throws Exception {
        // given
        LoginRequest loginRequest = LoginRequest.builder()
            .username("JIN HO")
            .password("1234")
            .build();

        // when
        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/sign-in")
                .content(objectMapper.writeValueAsString(loginRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());

        String content = result.andReturn()
            .getResponse()
            .getContentAsString();

        Success success = objectMapper.readValue(content, Success.class);
        Map<String, String> data = (Map<String, String>) success.data();

        String accessToken = data.get("accessToken");

        assertEquals(TokenStatus.IS_VALID, tokenProvider.validateAccessToken(accessToken));

    }

    @Test
    void 로그인_실패_테스트() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
            .username("TAE HO")
            .password("12345")
            .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                .content(objectMapper.writeValueAsString(loginRequest))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code")
                .value(ErrorCode.INVALID_CREDENTIALS.getCode())
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                .value(ErrorCode.INVALID_CREDENTIALS.getMessage())
            );

    }

    @Test
    void 회원가입_성공_테스트() throws Exception {
        // given
        SignUpUserRequest request = SignUpUserRequest.builder()
            .username("TAE HO")
            .password("1234")
            .nickname("TOOTH")
            .build();

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-up")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        result
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("TAE HO"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value("TOOTH"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value("USER"))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));


    }

    @Test
    void 회원가입_실패_테스트() throws Exception {
        // given
        SignUpUserRequest request = SignUpUserRequest.builder()
            .username("JIN HO")
            .password("1234")
            .nickname("EYES")
            .build();

        // when
        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/sign-up")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code")
                .value(ErrorCode.USER_ALREADY_EXISTS.getCode()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                .value(ErrorCode.USER_ALREADY_EXISTS.getMessage()));


    }

    private void setUpUser() {
        SignUpUserRequest request = SignUpUserRequest.builder()
            .username("JIN HO")
            .password("1234")
            .nickname("Mentos")
            .build();

        userService.save(request.toCommand());
        System.out.println("token = " + adminToken);
    }

    private void generateAdminToken() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
            .username("admin")
            .password("12341234")
            .build();

        String response = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/sign-in")
                    .content(objectMapper.writeValueAsString(loginRequest))
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        ;

        Success success = objectMapper.readValue(response, Success.class);

        Map<String, String> data = (Map<String, String>) success.data();
        adminToken = data.get("accessToken");
    }
}
