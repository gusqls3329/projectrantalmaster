package com.team5.projrental.dispute;

import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.dispute.model.DisputeDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DisputeControllerTest {


    @Mock
    DisputeService disputeService;

    @InjectMocks
    DisputeController disputeController;


    @Test
    void postDisputeProduct() {

        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");


        Long checkNum = -1000L;
        when(disputeService.postDisputeProduct(any())).thenReturn(new ResVo(checkNum));
        ResVo resVo = disputeController.postDisputeProduct(disputeDto);
        assertThat(resVo.getResult()).isEqualTo(checkNum);
    }

    @Test
    void postDisputeUser() {

        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");

        Long checkNum = -1000L;
        when(disputeService.postDisputeUser(any())).thenReturn(new ResVo(checkNum));

        ResVo resVo = disputeController.postDisputeUser(disputeDto);
        assertThat(resVo.getResult()).isEqualTo(checkNum);

    }

    @Test
    void postDisputeChat() {

        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");

        Long checkNum = -1000L;
        when(disputeService.postDisputeChat(any())).thenReturn(new ResVo(checkNum));

        ResVo resVo = disputeController.postDisputeChat(disputeDto);
        assertThat(resVo.getResult()).isEqualTo(checkNum);
    }

    @Test
    void postDisputePayment() {

        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");

        Long checkNum = -1000L;
        when(disputeService.postDisputePayment(any())).thenReturn(new ResVo(checkNum));

        ResVo resVo = disputeController.postDisputePayment(disputeDto);
        assertThat(resVo.getResult()).isEqualTo(checkNum);
    }

    @Test
    void postDisputeBoard() {

        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");

        Long checkNum = -1000L;
        when(disputeService.postDisputeBoard(any())).thenReturn(new ResVo(checkNum));

        ResVo resVo = disputeController.postDisputeBoard(disputeDto);
        assertThat(resVo.getResult()).isEqualTo(checkNum);
    }
}