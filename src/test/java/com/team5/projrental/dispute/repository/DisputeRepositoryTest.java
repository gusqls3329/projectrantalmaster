package com.team5.projrental.dispute.repository;

import com.team5.projrental.entities.*;
import com.team5.projrental.entities.enums.ChatUserStatus;
import com.team5.projrental.entities.enums.DisputeReason;
import com.team5.projrental.entities.enums.DisputeStatus;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.payment.thirdproj.PaymentRepository;
import com.team5.projrental.payment.thirdproj.paymentinfo.PaymentInfoRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.dialect.MySQLServerConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


//@Import(MySQLServerConfiguration.class)
@SpringBootTest
@Transactional
class DisputeRepositoryTest {

    @Autowired
    ChatUserRepository chatUserRepository;
    @Autowired
    DisputeBoardRepository disputeBoardRepository;
    @Autowired
    DisputeChatUserRepository disputeChatUserRepository;
    @Autowired
    DisputePaymentRepository disputePaymentRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentInfoRepository paymentInfoRepository;
    @Autowired
    DisputeProductRepository disputeProductRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    DisputeUserRepository disputeUserRepository;
    @Autowired
    UserRepository userRepository;


    User testUser;

    @BeforeEach
    void before() {
        this.testUser = userRepository.findById(16L).get();

    }

    @Test
    void chatUserRepository() {

        ChatUser findChatUser =
                chatUserRepository.findAll().stream().filter(cu -> Objects.equals(cu.getUser().getId(), testUser.getId())).toList().get(0);
        Assertions.assertThat(findChatUser.getUser().getId()).isEqualTo(testUser.getId());

    }

    @Test
    void disputeBoardRepository() {
        DisputeBoard saveDisputeBoard = new DisputeBoard();
        DisputeBoard mapDisputeBoard = disputeBoardRepository.findAll().get(0);
        saveDisputeBoard.setBoard(mapDisputeBoard.getBoard());
        saveDisputeBoard.setReason(DisputeReason.MANNER);
        saveDisputeBoard.setPenalty((byte) DisputeReason.MANNER.getPenaltyScore());
        saveDisputeBoard.setReporter(testUser);
        saveDisputeBoard.setReportedUser(mapDisputeBoard.getBoard().getUser());
        saveDisputeBoard.setStatus(DisputeStatus.STAND_BY);
        disputeBoardRepository.save(saveDisputeBoard);

        Long disputeBoardId = saveDisputeBoard.getId();

        disputeBoardRepository.flush();

        DisputeBoard findDisputeBoard = disputeBoardRepository.findById(disputeBoardId).get();

        Assertions.assertThat(findDisputeBoard.getReporter().getId()).isEqualTo(testUser.getId());

    }

    @Test
    void disputeChatUserRepository() {

        DisputeChatUser saveDisputeChatUser = new DisputeChatUser();
        ChatUser mapChatUser = chatUserRepository.findAll().get(0);
        saveDisputeChatUser.setChatUser(mapChatUser);
        saveDisputeChatUser.setReason(DisputeReason.MANNER);
        saveDisputeChatUser.setPenalty((byte) DisputeReason.MANNER.getPenaltyScore());
        saveDisputeChatUser.setReporter(testUser);
        saveDisputeChatUser.setReportedUser(mapChatUser.getUser());
        saveDisputeChatUser.setStatus(DisputeStatus.STAND_BY);
        disputeChatUserRepository.save(saveDisputeChatUser);
        Long disputeChatUserId = saveDisputeChatUser.getId();
        disputeBoardRepository.flush();

        DisputeChatUser findDisputeChatUser = disputeChatUserRepository.findById(disputeChatUserId).get();
        Assertions.assertThat(findDisputeChatUser.getReporter().getId()).isEqualTo(testUser.getId());

    }

    @Test
    void disputePaymentRepository() {

        DisputePayment saveDisputePayment = new DisputePayment();
        PaymentInfo mapPaymentInfo = paymentInfoRepository.findAll().get(0);
        saveDisputePayment.setPaymentInfo(mapPaymentInfo);
        saveDisputePayment.setReason(DisputeReason.MANNER);
        saveDisputePayment.setPenalty((byte) DisputeReason.MANNER.getPenaltyScore());
        saveDisputePayment.setReporter(testUser);
        saveDisputePayment.setReportedUser(mapPaymentInfo.getUser());
        saveDisputePayment.setStatus(DisputeStatus.STAND_BY);
        disputePaymentRepository.save(saveDisputePayment);
        Long disputePaymentId = saveDisputePayment.getId();
        disputeBoardRepository.flush();

        DisputePayment findDisputePayment = disputePaymentRepository.findById(disputePaymentId).get();
        Assertions.assertThat(findDisputePayment.getReporter().getId()).isEqualTo(testUser.getId());

    }

    @Test
    void disputeProductRepository() {

        DisputeProduct saveDisputeProduct = new DisputeProduct();
        Product mapProduct = productRepository.findAll().get(0);
        saveDisputeProduct.setProduct(mapProduct);
        saveDisputeProduct.setReason(DisputeReason.MANNER);
        saveDisputeProduct.setPenalty((byte) DisputeReason.MANNER.getPenaltyScore());
        saveDisputeProduct.setReporter(testUser);
        saveDisputeProduct.setReportedUser(mapProduct.getUser());
        saveDisputeProduct.setStatus(DisputeStatus.STAND_BY);
        disputeProductRepository.save(saveDisputeProduct);
        Long disputeProductId = saveDisputeProduct.getId();
        disputeBoardRepository.flush();

        DisputeProduct findDisputeProduct = disputeProductRepository.findById(disputeProductId).get();
        Assertions.assertThat(findDisputeProduct.getReporter().getId()).isEqualTo(testUser.getId());

    }

    @Test
    void disputeUserRepository() {

        DisputeUser saveUser = new DisputeUser();
        User mapUser = userRepository.findAll().get(0);
        saveUser.setUser(mapUser);
        saveUser.setReason(DisputeReason.MANNER);
        saveUser.setPenalty((byte) DisputeReason.MANNER.getPenaltyScore());
        saveUser.setReporter(testUser);
        saveUser.setReportedUser(mapUser);
        saveUser.setStatus(DisputeStatus.STAND_BY);
        disputeUserRepository.save(saveUser);
        Long disputeUserId = saveUser.getId();
        disputeBoardRepository.flush();

        DisputeUser findDisputeUser = disputeUserRepository.findById(disputeUserId).get();



    }

}