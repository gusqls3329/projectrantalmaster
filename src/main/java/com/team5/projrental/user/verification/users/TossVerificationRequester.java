package com.team5.projrental.user.verification.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpStatusCodes;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.entities.VerificationInfo;
import com.team5.projrental.user.verification.users.model.VerificationUserInfo;
import com.team5.projrental.user.verification.users.model.check.CheckRequestDto;
import com.team5.projrental.user.verification.users.model.check.CheckResponseVo;
import com.team5.projrental.user.verification.users.model.check.CheckResultDto;
import com.team5.projrental.user.verification.users.model.ready.VerificationReadyResponse;
import com.team5.projrental.user.verification.users.model.ready.VerificationReadyDto;
import com.team5.projrental.user.verification.users.model.ready.VerificationRequestDto;
import com.team5.projrental.user.verification.users.properties.TossVerificationProperties;
import im.toss.cert.sdk.TossCertSession;
import im.toss.cert.sdk.TossCertSessionGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class TossVerificationRequester {

    private final TossCertSessionGenerator generator = new TossCertSessionGenerator();
    private final TossVerificationProperties tossVerificationProperties;
    private final ObjectMapper om;

    public VerificationReadyDto verificationRequest(VerificationUserInfo userInfo) {

        // toss 에 전달할 Dto 생성
        VerificationRequestDto dto = encode(userInfo);

        // 요청
        String responseJSON = "";
        try {
            responseJSON = RestClient.builder()
                    .baseUrl(tossVerificationProperties.getReadyRequestUrl())
                    .build()
                    .post()
                    .header(tossVerificationProperties.getTokenKey(),
                            tossVerificationProperties.getTokenType() + tossVerificationProperties.getAccessToken())
                    .body(dto)
                    .contentType(MediaType.APPLICATION_JSON).retrieve().body(String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                e.printStackTrace();
                throw new ClientException(ErrorCode.BAD_INFO_EX_MESSAGE, "잘못된 인증 정보 입니다.");
            }
        }
        VerificationReadyResponse verificationReadyResponse;

        try {
            verificationReadyResponse = om.readValue(responseJSON, VerificationReadyResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return VerificationReadyDto.builder()
                .txid(verificationReadyResponse.getSuccess().getTxId())
                .success(verificationReadyResponse.getSuccess())
                .fail(verificationReadyResponse.getFail())
                .resultType(verificationReadyResponse.getResultType())
                .build();
    }

    private VerificationRequestDto encode(VerificationUserInfo userInfo) {
        TossCertSession session = generator.generate();

        return VerificationRequestDto.builder()
                .userName(session.encrypt(userInfo.getUserName()))
                .userPhone(session.encrypt(userInfo.getUserPhone()))
                .userBirthday(session.encrypt(userInfo.getUserBirthday()))
                .sessionKey(session.getSessionKey())
                .requestType(tossVerificationProperties.getRequestType())
                .triggerType(tossVerificationProperties.getTriggerType())
                .successCallbackUrl(tossVerificationProperties.getSuccessCallbackUrl())
                .failCallbackUrl(tossVerificationProperties.getFailCallbackUrl())
                .build();
    }

    public CheckResponseVo check(VerificationInfo info) {

        TossCertSession session = generator.generate();
        String sessionKey = session.getSessionKey();

        CheckRequestDto dto = CheckRequestDto.builder()
                .txId(info.getTxId())
                .sessionKey(sessionKey)
                .build();

        // 요청
        String responseJSON = "";
        try {
            responseJSON = RestClient.builder()
                    .baseUrl(tossVerificationProperties.getCheckRequestUrl())
                    .build().post()
                    .header(tossVerificationProperties.getTokenKey(),
                            tossVerificationProperties.getTokenType() +
                            tossVerificationProperties.getAccessToken())
                    .body(dto)
                    .contentType(MediaType.APPLICATION_JSON).retrieve().body(String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                e.printStackTrace();
                if (e.getMessage().contains("CE3102")) {
                    throw new ClientException(ErrorCode.BAD_INFO_EX_MESSAGE, "요청이 완료되지 않았습니다.");
                }
                throw new ClientException(ErrorCode.BAD_INFO_EX_MESSAGE, "잘못된 인증 정보 입니다.");
            }
        }
        CheckResultDto resultDto;

        try {
            resultDto = om.readValue(responseJSON, CheckResultDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (resultDto.getResultType().equalsIgnoreCase("FAIL")) {
            throw new ClientException(ErrorCode.BAD_INFO_EX_MESSAGE, "본인인증에 실패함");
        }

        return decode(session, resultDto);
    }

    private CheckResponseVo decode(TossCertSession session, CheckResultDto dto) {
        return CheckResponseVo.builder()
                .name(session.decrypt(dto.getSuccess().getPersonalData().getName()))
                .gender(session.decrypt(dto.getSuccess().getPersonalData().getGender()))
                .birthday(session.decrypt(dto.getSuccess().getPersonalData().getBirthday()))
                .nationality(session.decrypt(dto.getSuccess().getPersonalData().getNationality()))
                .build();
    }
}
