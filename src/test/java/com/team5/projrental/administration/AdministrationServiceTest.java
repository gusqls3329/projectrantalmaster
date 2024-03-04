package com.team5.projrental.administration;

import com.team5.projrental.admin.AdminRepository;
import com.team5.projrental.administration.model.UserByAdminVo;
import com.team5.projrental.administration.repository.*;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.dispute.repository.DisputeChatUserRepository;
import com.team5.projrental.entities.Admin;
import com.team5.projrental.entities.Board;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.enums.UserStatus;
import com.team5.projrental.payment.thirdproj.paymentinfo.PaymentInfoRepository;
import com.team5.projrental.payment.thirdproj.refund.RefundRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AdministrationServiceTest {

    @InjectMocks
    AdministrationService administrationService;

    @Mock
    ApplicationEventPublisher eventPublisher;
    @Mock
    PaymentInfoRepository paymentInfoRepository;
    @Mock
    AdminUserRepository userRepository;
    @Mock
    AdminRepository adminRepository;
    @Mock
    AuthenticationFacade facade;
    @Mock
    AdminBoardRepository boardRepository;
    @Mock
    ResolvedBoardRepository resolvedBoardRepository;
    @Mock
    ResolvedUserRepository resolvedUserRepository;
    @Mock
    ResolvedProductRepository resolvedProductRepository;
    @Mock
    AdminDisputeRepository adminDisputeRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    RefundRepository refundRepository;
    @Mock
    DisputeChatUserRepository disputeChatUserRepository;




    @Test
    void getAllUsers() {
        Long checkNum = -100L;
        when(userRepository.totalCountByOptions(any(), any(), any())).thenReturn(checkNum);
        when(userRepository.findUserByOptions(any(), any(), any(), any())).thenReturn(new ArrayList<>());

        UserByAdminVo result = administrationService.getAllUsers(1, 1, "test", 1);

        assertThat(result.getTotalUserCount()).isEqualTo(checkNum);

    }


    @Test
    void getAllDispute() {
        Long checkNum = -100L;
        when(adminDisputeRepository.totalCountByOptions(any(), any(), any(), any())).thenReturn(checkNum);
        when(adminDisputeRepository.findByLimitPage(any(), any(), any(), any(), any())).thenReturn(new ArrayList<>());



    }

    @Test
    void postDispute() {
    }

    @Test
    void getAllProducts() {
    }

    @Test
    void patchProduct() {
    }

    @Test
    void patchRefund() {
    }

    @Test
    void getAllBoards() {
    }

    @Test
    void getAllRefunds() {
    }

    @Test
    void getAllChats() {
    }

    @Test
    void changeUserWhenOverPenalty() {
    }
}