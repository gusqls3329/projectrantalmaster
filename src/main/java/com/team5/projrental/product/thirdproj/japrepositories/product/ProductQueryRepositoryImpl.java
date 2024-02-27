package com.team5.projrental.product.thirdproj.japrepositories.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.*;
import com.team5.projrental.entities.enums.ProductMainCategory;
import com.team5.projrental.entities.enums.ProductStatus;
import com.team5.projrental.entities.enums.ProductSubCategory;
import com.team5.projrental.entities.ids.ProdLikeIds;
import com.team5.projrental.product.like.ProductLikeMapper;
import com.team5.projrental.product.model.CanNotRentalDateVo;
import com.team5.projrental.product.model.Categories;
import com.team5.projrental.product.model.ProductListVo;
import com.team5.projrental.product.model.jpa.ActivatedStock;
import com.team5.projrental.product.thirdproj.japrepositories.product.like.ProductLikeRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.stock.StockRepository;
import com.team5.projrental.product.thirdproj.model.ProductListForMainDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.team5.projrental.common.Const.PROD_PER_PAGE;
import static com.team5.projrental.entities.QPayment.payment;
import static com.team5.projrental.entities.QPaymentInfo.paymentInfo;
import static com.team5.projrental.entities.QProduct.product;
import static com.team5.projrental.entities.QReview.review;
import static com.team5.projrental.entities.enums.PaymentInfoStatus.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;
    private final StockRepository stockRepository;
    private final ProductLikeRepository productLikeRepository;
    private final ProductLikeMapper productLikeMapper;
    private final JdbcTemplate jdbcTemplate;

    public Map<Long, List<ActivatedStock>> getActivatedStock(LocalDateTime refDate) {

        LocalDateTime from = LocalDateTime.of(refDate.getYear(), refDate.getMonth(), refDate.getMonthValue(), 0, 0, 0);
        LocalDateTime toBuilder = refDate.plusMonths(Const.SEARCH_ACTIVATED_MONTH_DURATION);
        LocalDateTime to = LocalDateTime.of(toBuilder.getYear(), toBuilder.getMonth(), toBuilder.toLocalDate().lengthOfMonth(),
                0, 0, 0);

        QProduct product = QProduct.product;
        List<Long> iproducts = query.select(product.id)
                .from(product)
                .orderBy(product.id.desc())
                .offset(0)
                .limit(Const.ACTIVATED_CACHE_MAX_NUM)
                .fetch();
        Map<Long, List<ActivatedStock>> result = new HashMap<>();

        for (Long iproduct : iproducts) {
            LocalDateTime dateWalker = LocalDateTime.of(from.getYear(), from.getMonth(), from.getMonthValue(), 0, 0, 0);
            QStock stock = QStock.stock;
            QPayment payment = QPayment.payment;

            List<ActivatedStock> activatedStocks = new ArrayList<>();

            while (!dateWalker.equals(to)) {
                List<Stock> findStock = query.select(stock)
                        .from(stock)
                        .join(stock)
                        .on(stock.product.status.eq(ProductStatus.ACTIVATED))
                        .join(payment)
                        .on(payment.rentalDates.rentalStartDate.after(from).and(payment.rentalDates.rentalEndDate.before(from)))
                        .fetch();

                if (!findStock.isEmpty()) {
                    activatedStocks.add(ActivatedStock.builder()
                            .date(dateWalker)
                            // 상품중 activated 인 상품들의 재고를 가져오는데, refDate 가 결제의 rentalStartDate 와 rentalEndDate 사이가 아닌 재고만 가져오기
                            .activatedStock(findStock)
                            .build());
                }
                dateWalker = dateWalker.plusDays(1);
            }
            result.put(iproduct, activatedStocks);
        }
        return result;

    }

    @Override
    public List<ProductListVo> findAllBy(Integer sort, String search, ProductMainCategory mainCategory,
                                         ProductSubCategory subCategory, String addr, int page, Long iuser, int limit) {

        return query.select(product)
                .from(product)
                .where(whereSearchForFindAllBy(search, mainCategory, subCategory, addr)
                        .and(product.status.in(ProductStatus.ACTIVATED)))
                .offset(page)
                .limit(limit)
                .orderBy(orderByForFindAllBy(sort))
                .fetch()
                .stream()
                .map(productEntity -> {
                    int prodLikeCount = productLikeRepository.countByProdLikeIds(ProdLikeIds.builder()
                            .iuser(productEntity.getUser().getId())
                            .iproduct(productEntity.getId())
                            .build());
                    return ProductListVo.builder()
                            .iuser(productEntity.getUser().getId())
                            .nick(productEntity.getUser().getNick())
                            .userPic(productEntity.getUser().getBaseUser().getStoredPic())
                            .iproduct(productEntity.getId())
                            .title(productEntity.getTitle())
                            .prodMainPic(productEntity.getStoredPic())
                            .rentalPrice(productEntity.getRentalPrice())
                            .rentalStartDate(productEntity.getRentalDates().getRentalStartDate())
                            .rentalEndDate(productEntity.getRentalDates().getRentalEndDate())
                            .addr(productEntity.getAddress().getAddr())
                            .restAddr(productEntity.getAddress().getRestAddr())
                            .prodLike(prodLikeCount)
                            //FIXME -> enum 숫자 2차때랑 동일하게 변경. (ordinal 로 불가능한것은 value 설정 하기
                            .istatus(productEntity.getStatus().getNum())
                            .inventory(stockRepository.countById(productEntity.getId()))
                            .hashTags(productEntity.getHashTags().stream().map(HashTag::getTag).collect(Collectors.toList()))
                            .isLiked(prodLikeCount == 1 ? 1 : 0)
                            .view(productEntity.getView())
                            .categories(Categories.builder()
                                    .mainCategory(productEntity.getMainCategory().getNum())
                                    .subCategory(productEntity.getSubCategory().getNum())
                                    .build())
                            .build();
                }).collect(Collectors.toList());

    }


    @Override
    public List<ProductListForMainDto> findEachTop8ByCategoriesOrderByIproductDesc(int limitNum) {
        String query = """
                select
                        SQ.rn, U.iuser, U.nick, U.stored_pic as userPic,
                        SQ.iproduct, SQ.title, SQ.stored_pic as prodMainPic,
                        SQ.rental_price as rentalPrice, SQ.rental_start_date as rentalStartDate,
                        SQ.rental_end_date as rentalEndDate, SQ.addr, SQ.rest_addr as restAddr,
                        SQ.status, SQ.view, SQ.main_category as mainCategory,
                        SQ.sub_category as subCategory
                        from (
                        select row_number() over(partition by main_category, sub_category order by iproduct desc) rn,
                        P.iproduct, P.title, P.stored_pic,
                        P.rental_price, P.rental_start_date,
                        P.rental_end_date, P.addr, P.rest_addr,
                        P.status, P.view, P.main_category,
                        P.sub_category, P.iuser
                        from product P
                        where P.status = 'ACTIVATED'
                        order by P.iproduct desc
                        ) SQ
                        join USER U on U.iuser = SQ.iuser
                        where SQ.rn <= ?
                        order By SQ.main_category, SQ.sub_category, SQ.rn
                """;
        List<ProductListForMainDto> result = jdbcTemplate.query(query,
                BeanPropertyRowMapper.newInstance(ProductListForMainDto.class),
                limitNum);

//        List<ProductListForMainDto> result = productLikeMapper.findEachTop8ByCategoriesOrderByIproductDesc(limitNum);

        log.info("result = {}", result);
        return result;
    }

    @Override
    public List<Product> findByUser(User user, Integer page) {

        return query.select(product)
                .from(product)
                .join(product.user).fetchJoin()
                .leftJoin(product.prodLikes).fetchJoin()
                .where(product.user.eq(user))
                .offset(page)
                .limit(PROD_PER_PAGE)
                .fetch();

    }

    @Override
    public List<CanNotRentalDateVo> findCanNotRentalDateVoBy(Product product, LocalDate refStartDate, LocalDate refEndDate) {
        /*
        select PA.rental_start_date as rentalStartDate, PA.rental_end_date as rentalEndDate
        from payment PA
        JOIN product_payment PP ON PA.ipayment = PP.ipayment
        join payment_status PS on PA.ipayment = PS.ipayment
        where PP.iproduct = #{iproduct}
        and rental_end_date >= #{refStartDate}
        and rental_start_date <![CDATA[<=]]> #{refEndDate}
        and PS.istatus in (0, -4, 2, 3)
        group by PS.ipayment
         */

        List<CanNotRentalDateVo> result = query.selectFrom(payment)
                .join(paymentInfo).on(payment.eq(paymentInfo.payment))
                .where(
                        payment.product.eq(product)
                                .and(Expressions.stringTemplate("DATE_FORMAT({0}, {1})",
                                                payment.rentalDates.rentalEndDate, ConstantImpl.create("%Y-%m-%d"))
                                        .goe(refStartDate.toString())
                                )
                                .and(Expressions.stringTemplate("DATE_FORMAT({0}, {1})",
                                                payment.rentalDates.rentalStartDate, ConstantImpl.create("%Y-%m-%d"))
                                        .loe(refEndDate.toString())
                                )
                                .and(paymentInfo.status.in(ACTIVATED, RESERVED, EXPIRED))
                )
                .fetch().stream().map(payment -> CanNotRentalDateVo.builder()
                        .rentalStartDate(LocalDate.of(payment.getRentalDates().getRentalStartDate().getYear(),
                                payment.getRentalDates().getRentalStartDate().getMonth(),
                                payment.getRentalDates().getRentalStartDate().getDayOfMonth()))
                        .rentalEndDate(LocalDate.of(payment.getRentalDates().getRentalEndDate().getYear(),
                                payment.getRentalDates().getRentalEndDate().getMonth(),
                                payment.getRentalDates().getRentalEndDate().getDayOfMonth()))
                        .build()).collect(Collectors.toList());

        log.info("result = {}", result);

        return result;


    }

    @Override
    public Long countBySearchAndMainCategoryAndSubCategory(String search, Integer imainCategory, Integer isubCategory,
                                                           String addr) {
        ProductMainCategory mainCategory = ProductMainCategory.getByNum(imainCategory);
        return (long) query.select(product)
                .from(product)
                .where(whereSearchForFindAllBy(search,
                        mainCategory,
                        ProductSubCategory.getByNum(mainCategory, isubCategory),
                        addr))
                .fetch().size();
    }

    @Override
    public Long findByIuser(long iuser) {

        return (long) query.selectFrom(product)
                .where(product.id.eq(iuser))
                .fetch().size();

    }

    @Override
    public Long getReviewCount(Long iproduct) {

        return (long) query.select(review)
                .from(review)
                .join(payment).on(review.payment.eq(payment)).fetchJoin()
                .join(product).on(payment.product.eq(product)).fetchJoin()
                .where(product.id.eq(iproduct))
                .fetch()
                .size();

    }

    @Override
    public LocalDateTime findLastProductCreatedAtBy(User user) {

        List<LocalDateTime> fetch = query.select(product.createdAt)
                .from(product)
                .where(product.user.eq(user))
                .orderBy(product.id.desc())
                .limit(1)
                .fetch();

        if (fetch.isEmpty()) return null;

        return fetch.get(0);
    }

    @Override
    public List<Product> findAllLimitPage(int page, Integer type, String search) {

        return query.selectFrom(product)
                .join(product.user).fetchJoin()
                .where(product.status.notIn(ProductStatus.DELETED))
                .offset(page)
                .limit(Const.ADMIN_PER_PAGE)
                .fetch();

    }

    private BooleanBuilder whereFindAllLimitPage(Integer type, String search) {
        BooleanBuilder builder = new BooleanBuilder();
        if(type == null) return builder;
        if (type == 1) {
            builder.and(product.user.nick.like("%" + search + "%"));
        }
        return builder;
    }

    @Override
    public Optional<Product> findByIdJoinFetch(Long iproduct) {

        return Optional.ofNullable(query.selectFrom(product)
                .where(product.id.eq(iproduct).and(product.status.notIn(ProductStatus.DELETED)))
                .fetchOne());
    }


    private BooleanBuilder whereSearchForFindAllBy(String search, ProductMainCategory mainCategory,
                                                   ProductSubCategory subCategory) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (search != null) {
            booleanBuilder.and(product.title.like("%" + search + "%"));
        }

        booleanBuilder.and(product.mainCategory.eq(mainCategory));
        if (subCategory != null) {
            booleanBuilder.and(product.subCategory.eq(subCategory));
        }
        return booleanBuilder;
    }

    private BooleanBuilder whereSearchForFindAllBy(String search, ProductMainCategory mainCategory,
                                                   ProductSubCategory subCategory, String addr) {
        BooleanBuilder booleanBuilder = whereSearchForFindAllBy(search, mainCategory, subCategory);
        if (addr != null && !addr.isEmpty()) {
            // 대구 달서구 용산1동 -> 대구%달서구%용산1동%
            String likeAddr = addr.trim();
            if (addr.contains("_")) {
                likeAddr = likeAddr.replaceAll("_", "%");
            } else {
                likeAddr = likeAddr.replaceAll(" ", "%");
            }
            likeAddr += "%";
            booleanBuilder.and(product.address.addr.like(likeAddr));
        }
        return booleanBuilder;
    }

    private OrderSpecifier<? extends Number> orderByForFindAllBy(Integer sort) {
        return sort == null ? product.id.desc() : sort == 1 ? product.prodLikes.size().desc() : product.view.desc();
    }


}