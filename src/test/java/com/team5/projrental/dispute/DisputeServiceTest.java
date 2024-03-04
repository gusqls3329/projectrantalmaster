package com.team5.projrental.dispute;

import com.team5.projrental.board.BoardRepository;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.dispute.model.DisputeDto;
import com.team5.projrental.dispute.repository.*;
import com.team5.projrental.entities.*;
import com.team5.projrental.payment.thirdproj.paymentinfo.PaymentInfoRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


//@SpringBootTest
@ExtendWith(SpringExtension.class)
//@Import(DisputeService.class)
//@Transactional
@WithAnonymousUser
class DisputeServiceTest {


    @Mock
    AuthenticationFacade facade;
    @Mock
    UserRepository userRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    DisputeProductRepository disputeProductRepository;
    @Mock
    DisputeUserRepository disputeUserRepository;
    @Mock
    ChatUserRepository chatUserRepository;
    @Mock
    DisputeChatUserRepository disputeChatUserRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    DisputeBoardRepository disputeBoardRepository;
    @Mock
    PaymentInfoRepository paymentInfoRepository;
    @Mock
    DisputePaymentRepository disputePaymentRepository;


    @InjectMocks
    DisputeService disputeService;


    @Test
    void postDisputeProduct() {
        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");


        when(facade.getLoginUserPk()).thenReturn(16L);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(new User()));
        when(productRepository.findJoinFetchById(any())).thenReturn(Optional.of(new Product()));
        when(disputeProductRepository.save(any())).thenReturn(null);


        ResVo resVo = disputeService.postDisputeProduct(disputeDto);
        System.out.println("resVo.getResult() = " + resVo.getResult());
        Assertions.assertThat(resVo.getResult()).isEqualTo(1L);


    }

    @Test
    void postDisputeUser() {

        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");

        when(facade.getLoginUserPk()).thenReturn(16L);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(new User()));
        when(disputeUserRepository.save(any())).thenReturn(null);

        ResVo resVo = disputeService.postDisputeUser(disputeDto);
        System.out.println("resVo.getResult() = " + resVo.getResult());
    }

    @Test
    void postDisputeChat() {

        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");

        when(facade.getLoginUserPk()).thenReturn(16L);
        when(chatUserRepository.findById(any())).thenReturn(java.util.Optional.of(new ChatUser()));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(new User()));
        when(chatUserRepository.findByIchatAndNeUser(any(), any())).thenReturn(java.util.Optional.of(new ChatUser()));
        when(disputeChatUserRepository.save(any())).thenReturn(null);

        ResVo resVo = disputeService.postDisputeChat(disputeDto);
        System.out.println("resVo.getResult() = " + resVo.getResult());

    }

    @Test
    void postDisputeBoard() {

        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");

        when(facade.getLoginUserPk()).thenReturn(16L);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(new User()));
        when(boardRepository.findByIdJoinFetch(any())).thenReturn(java.util.Optional.of(Board.builder().build()));
        when(disputeBoardRepository.save(any())).thenReturn(null);

        ResVo resVo = disputeService.postDisputeBoard(disputeDto);
        System.out.println("resVo.getResult() = " + resVo.getResult());

    }

    @Test
    void postDisputePayment() {

        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setReason(1);
        disputeDto.setIdentity(10L);
        disputeDto.setDetails("test");

        when(facade.getLoginUserPk()).thenReturn(16L);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(new User()));
        when(paymentInfoRepository.findByIdJoinFetch(any(), any(), any())).thenReturn(java.util.Optional.of(PaymentInfo.builder().build()));
        when(disputePaymentRepository.save(any())).thenReturn(null);

        ResVo resVo = disputeService.postDisputePayment(disputeDto);
        System.out.println("resVo.getResult() = " + resVo.getResult());

    }
}