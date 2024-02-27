package com.team5.projrental.mockdata;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.*;
import com.team5.projrental.entities.embeddable.Address;
import com.team5.projrental.entities.embeddable.RentalDates;
import com.team5.projrental.entities.enums.*;
import com.team5.projrental.entities.ids.PaymentInfoIds;
import com.team5.projrental.entities.ids.ProdLikeIds;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.entities.mappedsuper.BaseUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Profile({"default", "hyunmin", "prod"})
@Component
@RequiredArgsConstructor
public class MockDataRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() throws InterruptedException {

        VerificationInfo verificationInfoTmp = VerificationInfo.builder()
                .txId("tx_id")
                .userBirthday(LocalDate.of(1998, 1, 1).toString())
                .userName("테스트 유저")
                .userPhone("010-1111-" + 1111)
                .build();
        User userTmp = new User(BaseUser.builder()
                .storedPic("dkowkoawrr.jpg")
                .rating(4.1)
                .address(Address.builder()
                        .addr("대구 중구 서성로 26 정무빌딩")
                        .restAddr("어딘가")
                        .x(1245.12)
                        .y(125125.522)
                        .build()).build(), verificationInfoTmp, UserStatus.ACTIVATED, "nickkk", ProvideType.LOCAL, (byte) 0);
        userTmp.setEmail("test@test.com");
        userTmp.setPhone("010-1111-" + 1111);
        userTmp.setUid("test");
        userTmp.setUpw("$2a$10$4OxWp8Q.ghCbheS732cYhu7ZVBGNTdrgl8uHjpCnvAxjRIZFczsmu");
        userTmp.setAuth(Auth.USER);

        em.persist(userTmp);
        em.flush();
        em.clear();

        for (int i = 0; i < 100; i++) {
            VerificationInfo verificationInfo = VerificationInfo.builder()
                    .txId("tx_id" + i)
                    .userBirthday(LocalDate.of(1998, 1, 1).plusDays(i).toString())
                    .userName("조현민" + i)
                    .userPhone("010-1111-" + 1111 + i)
                    .build();
            User user = new User(BaseUser.builder()
                    .storedPic("dkowkoawrr.jpg")
                    .rating(0.1 + (i % 4 + 1))
                    .address(Address.builder()
                            .addr("대구 중구 서성로 26 정무빌딩")
                            .restAddr("어딘가" + i)
                            .x(1245.12 + i)
                            .y(125125.522 + i)
                            .build()).build(), verificationInfo, UserStatus.ACTIVATED, "nick" + i, ProvideType.LOCAL, (byte) 0);
            user.setEmail(i + "test@test.com");
            user.setPhone("010-1111-" + 1111 + i);
            user.setUid("test" + i);
            user.setUpw("test" + i);
            user.setAuth(Auth.USER);

            em.persist(user);
            Admin admin = new Admin("운영자", "asetaset", AdminStatus.ACTIVE);
            admin.setEmail(i * i + "test@test.com");
            admin.setPhone("010-1111-" + 1111 + i);
            admin.setUid(i + "test" + i);
            admin.setUpw(i + "test" + i);
            admin.setAuth(Auth.ADMIN);
            em.persist(admin);


        }
        for (int i = 0; i < 100; i++) {
            int rnum1 = (int) (Math.random() * 100) + 1;
            User user = em.find(User.class, rnum1);
            while (user == null) {
                rnum1 = (int) (Math.random() * 100) + 1;
                user = em.find(User.class, rnum1);
            }
            Product product = Product.builder()
                    .storedPic("kjaojropwrj.jpeg")
                    .rentalDates(RentalDates.builder()
                            .rentalStartDate(LocalDateTime.of(2024, 1, 1, 0, 0, 0).plusDays(i))
                            .rentalEndDate(LocalDateTime.of(2050, 1, 1, 0, 0, 0).plusDays(i))
                            .build())
                    .user(user)
                    .address(Address.builder()
                            .addr("대구 중구 서성로 26 정무빌딩")
                            .restAddr("그린" + i)
                            .x(353.23153215 + i)
                            .y(3265236.23623 + i)
                            .build())
                    .status(ProductStatus.ACTIVATED)
                    .contents("하ㅏㅏ기ㅣㅣㅣ싫ㅎㅎㅎㅎ어ㅓㅓㅓㅓ" + i)
                    .title("ㅠㅠㅠㅠㅠㅠㅠㅠ" + i)
                    .mainCategory(i % 4 == 0 ? ProductMainCategory.CAMERA : i % 3 == 0 ? ProductMainCategory.SMART :
                            i % 2 == 0 ? ProductMainCategory.SOUND : ProductMainCategory.PC_AND_LAPTOP)
                    .subCategory(i % 4 == 0 ? ProductSubCategory.CAMERA : i % 3 == 0 ? ProductSubCategory.IPHONE :
                            i % 2 == 0 ? ProductSubCategory.HEADPHONE : ProductSubCategory.KEYBOARD)
                    .rentalPrice(125215 * i)
                    .view(0L)
                    .build();
            Stock stock = Stock.builder()
                    .product(product)
                    .seq(1L)
                    .build();
            product.getStocks().add(stock);
            em.persist(product);
            List<String> tags = List.of("태그1", "태그2", "태그3", "태그5", "태그6", "태그7");
            HashTag hashTag = HashTag.builder()
                    .product(product)
                    .tag("하하하하" + tags.get(i % 6))
                    .build();
            em.persist(hashTag);
            em.flush();
            em.clear();
        }

        for (int i = 0; i < 20; i++) {
            int rnum1 = (int) (Math.random() * 100) + 1;
            int rnum2 = (int) (Math.random() * 100) + 1;

            Product product = em.find(Product.class, rnum1);

            User user = em.find(User.class, rnum2);
            while (user == null) {
                rnum2 = (int) (Math.random() * 100) + 1;
                user = em.find(User.class, rnum2);
            }
            ProdLike prodLike = ProdLike.builder()
                    .prodLikeIds(ProdLikeIds.builder()
                            .iuser(user.getId())
                            .iproduct(product.getId())
                            .build())
                    .product(product)
                    .user(user)
                    .build();
            List<ProdLike> resultList = em.createQuery(
                            "select pl " +
                            "from ProdLike pl " +
                            "where pl.prodLikeIds.iproduct = :iproduct and pl.prodLikeIds.iuser = :iuser",
                            ProdLike.class)
                    .setParameter("iproduct", prodLike.getProdLikeIds().getIproduct())
                    .setParameter("iuser", prodLike.getProdLikeIds().getIuser())
                    .getResultList();
            if (!resultList.isEmpty()) {
                continue;
            }
            em.persist(prodLike);
            em.flush();
            em.clear();
        }
        LocalDate startDate = LocalDate.now().minusDays(40);
        for (int i = 0; i < 40; i++) {
            int rnum1 = (int) (Math.random() * 100) + 1;
            int rnum2 = (int) (Math.random() * 100) + 1;
            int rnum3 = (int) (Math.random() * 100) + 1;
            LocalDate endDate = startDate.plusDays(rnum3);

            User user = em.find(User.class, rnum1);
            while (user == null) {
                rnum1 = (int) (Math.random() * 100) + 1;
                user = em.find(User.class, rnum1);
            }
            Product product = em.find(Product.class, rnum2);

            while (product == null) {
                rnum2 = (int) (Math.random() * 100) + 1;
                product = em.find(Product.class, rnum2);
            }
            String code = "";
            List<String> codeList = List.of("q", "t", "1", "r", "w", "7", "4", "9", "0", "w", "v", "l", "o", "p", "y", "v", "G", "A", "w", "B",
                    "W", "E", "w", "o", "b", "m", "h");
            for (int j = 0; j < 10; j++) {
                code += codeList.get((int) (Math.random() * codeList.size()));
                List<Payment> codes = em.createQuery("select pa from Payment pa where pa.code = :code", Payment.class)
                        .setParameter("code", code)
                        .getResultList();
                if (!codes.isEmpty()) {
                    --j;
                }
            }


            Payment payment = Payment.builder()
                    .product(product)
                    .user(user)
                    .code(code)
                    .rentalDates(RentalDates.builder()
                            .rentalStartDate(LocalDateTime.of(startDate, LocalTime.of(0, 0, 0)))
                            .rentalEndDate(LocalDateTime.of(endDate, LocalTime.of(23, 59, 59)))
                            .build())
                    .stock(product.getStocks().get(0))
                    .deposit(41425 + i)
                    .totalPrice((int) (ChronoUnit.DAYS.between(startDate, endDate) + 1) * product.getRentalPrice())
                    .duration((int) (ChronoUnit.DAYS.between(startDate, endDate) + 1))
                    .paymentDetail(PaymentDetail.builder()
                            .tid("testTid")
                            .category(PaymentDetailCategory.KAKAO_PAY)
                            .user(user)
                            .build())
                    .build();

            payment.getPaymentDetail().setAmount(payment.getTotalPrice() + payment.getDeposit());

            PaymentInfo paymentInfo = PaymentInfo.builder()
                    .paymentInfoIds(PaymentInfoIds.builder()
                            .ipayment(payment.getId())
                            .iuser(user.getId())
                            .build())
                    .user(user)
                    .code(genUserPaymentCode())
                    .payment(payment)
                    .build();
            User diffUser;
            while (true) {
                int rnum4 = (int) (Math.random() * 100) + 1;
                diffUser = em.find(User.class, rnum4);
                if (diffUser == null) {
                    continue;
                }
                if (!Objects.equals(user.getId(), diffUser.getId())) {
                    break;
                }
            }

            PaymentInfo paymentInfo2 = PaymentInfo.builder()
                    .paymentInfoIds(PaymentInfoIds.builder()
                            .ipayment(payment.getId())
                            .iuser(user.getId())
                            .build())
                    .user(diffUser)
                    .code(genUserPaymentCode())
                    .payment(payment)
                    .build();

            em.persist(payment);
            em.persist(paymentInfo);
            em.persist(paymentInfo2);
            if (i % 4 == 0) {
                Review review = Review.builder()
                        .contents("댓글ㄹㄹ")
                        .status(ReviewStatus.ACTIVATED)
                        .user(user)
                        .payment(payment)
                        .rating((i % 5 + 1))
                        .build();
                em.persist(review);
                em.flush();
                em.clear();
            }
        }

        List<Review> reviews =
                em.createQuery("select r from Review r", Review.class).getResultList();

        for (Review review : reviews) {
            System.out.println("get: /api/prod/review/{iproduct} - 해당 제품에 작성된 모든 리뷰 = " +
                               review.getPayment().getProduct().getId());
        }

        em.createNativeQuery("insert into payment_detail(ipayment_detail, iuser, tid, category, created_at, amount) values(50, " +
                             "1, 'egio', 'KAKAO_PAY', now(), 197500)").executeUpdate();

        em.flush();
        em.clear();

        Admin findAdmin = em.createQuery("select a from Admin a where a.uid = '0test0'", Admin.class)
                .getSingleResult();
        findAdmin.setUpw("$2a$10$4OxWp8Q.ghCbheS732cYhu7ZVBGNTdrgl8uHjpCnvAxjRIZFczsmu");

        em.flush();
        em.clear();

        /*
INSERT INTO board (`created_at`, `iboard`, `iuser`, `updated_at`, `view`, `contents`, `title`) VALUES ('2024-02-18 10:14:27', 1, 1, '2024-02-18 10:15:30', 2, '안녕하세요! 잘부탁해요', '새로가입했어요!');
INSERT INTO board_comment (`created_at`, `iboard`, `iboard_comment`, `iuser`, `updated_at`, `comment`)
VALUES ('2024-02-18 10:14:27', 1, 1, 2, '2024-02-18 13:14:27', '오 잘부탁해요!');
INSERT INTO board_like (`board_iboard`, `created_at`, `iuser`) VALUES (1, '2024-02-19 13:14:27', 2);
INSERT INTO board_pic (`created_at`, `iboard`, `ipics`, `stored_pic`) VALUES ('2024-02-19 13:14:27', 1, 1, 'propic.jpeg');

         */

        User findUser = em.createQuery("select u from User u where u.uid = 'test7'", User.class).getSingleResult();
        User commentUser = em.createQuery("select u from User u where u.uid = 'test8'", User.class).getSingleResult();

        Board saveBoard = Board.builder()
                .user(findUser)
                .view(0L)
                .contents("etest")
                .title("etst")
                .status(BoardStatus.ACTIVATED)
                .build();
        saveBoard.setCreatedAt(LocalDateTime.now());
        saveBoard.setUpdatedAt((LocalDateTime.now()));

        BoardComment saveBoardComment = BoardComment.builder()
                .board(saveBoard)
                .user(commentUser)
                .comment("testasetcomment")
                .build();

        saveBoardComment.setCreatedAt(LocalDateTime.now());
        saveBoardComment.setUpdatedAt((LocalDateTime.now()));
        em.persist(saveBoardComment);
        em.flush();
        em.clear();


        em.createQuery("select us from Users us where us.uid = '7test7'", Users.class).getSingleResult()
                .setUpw("$2a$10$4OxWp8Q.ghCbheS732cYhu7ZVBGNTdrgl8uHjpCnvAxjRIZFczsmu");
        em.createQuery("select us from Users us where us.uid = 'test7'", Users.class).getSingleResult()
                .setUpw("$2a$10$4OxWp8Q.ghCbheS732cYhu7ZVBGNTdrgl8uHjpCnvAxjRIZFczsmu");
        em.flush();
        em.clear();
    }

    private String genUserPaymentCode() {
        String uuid = UUID.randomUUID().toString().substring(0, 10);
        List<PaymentInfo> result = em.createQuery("""
                                        select pi from PaymentInfo pi where pi.code = :uuid
                        """
                , PaymentInfo.class).setParameter("uuid", uuid).getResultList();
        if (!result.isEmpty()) {
            return genUserPaymentCode();
        }
        return uuid;
    }

}