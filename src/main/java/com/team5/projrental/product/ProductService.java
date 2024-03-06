package com.team5.projrental.product;

import com.team5.projrental.common.Const;
import com.team5.projrental.common.aop.anno.CountView;
import com.team5.projrental.common.aop.category.CountCategory;
import com.team5.projrental.common.exception.*;
import com.team5.projrental.common.exception.base.*;
import com.team5.projrental.common.exception.checked.FileNotContainsDotException;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.model.restapi.Addrs;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.common.utils.AxisGenerator;
import com.team5.projrental.common.utils.CommonUtils;
import com.team5.projrental.common.utils.MyFileUtils;
import com.team5.projrental.entities.*;
import com.team5.projrental.entities.embeddable.Address;
import com.team5.projrental.entities.embeddable.RentalDates;
import com.team5.projrental.entities.enums.ProductMainCategory;
import com.team5.projrental.entities.enums.ProductStatus;
import com.team5.projrental.entities.enums.ProductSubCategory;
import com.team5.projrental.product.model.*;
import com.team5.projrental.product.model.proc.*;
import com.team5.projrental.product.model.review.ReviewResultVo;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.like.ProductLikeRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.pics.ProductPicsRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.review.ProductReviewRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.stock.StockRepository;
import com.team5.projrental.product.thirdproj.model.ProductListForMainDto;
import com.team5.projrental.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.team5.projrental.common.Const.*;
import static com.team5.projrental.common.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService implements RefProductService {

    private final RefProductRepository refProductRepository;

    private final AxisGenerator axisGenerator;

    private final AuthenticationFacade authenticationFacade;

    private final MyFileUtils myFileUtils;

    private final ProductRepository productRepository;

    private final ProductLikeRepository productLikeRepository;

    private final ProductPicsRepository productPicsRepository;

    private final ProductReviewRepository productReviewRepository;

    private final UserRepository userRepository;

    private final StockRepository stockRepository;
    /*
        ------- Logic -------
     */

    /**
     * 해당 카테고리의 전체 제품 조회.
     *
     * @param sort
     * @param search
     * @return List<ProductListVo>
     */
    public List<ProductListVo> getProductList(Integer sort,
                                              String search,
                                              int imainCategory,
                                              int isubCategory,
                                              String addr,
                                              int page,
                                              int prodPerPage) {
        // page 는 페이징에 맞게 변환되어 넘어옴.

        // 기본적인 파라미터 예외처리 (search 가 null 이면 카테고리가 필수다)
        if (search == null && imainCategory == 0) {
            throw new IllegalCategoryException(ILLEGAL_CATEGORY_EX_MESSAGE);
        }
        // iuser 가져오기 -> isLiked 를 위해서
        Long iuser = null;
        try {
            iuser = getLoginUserPk();
        } catch (ClassCastException ignored) {

        }
        if (imainCategory > 6 || isubCategory > 5) {
            throw new ClientException(ILLEGAL_CATEGORY_EX_MESSAGE, "잘못된 카테고리입니다.");
        }


        ProductMainCategory mainCategory = imainCategory > 0 ? ProductMainCategory.getByNum(imainCategory)
                : null;
        return productRepository.findAllBy(
                sort,
                search,
                mainCategory,
                isubCategory > 0 ? ProductSubCategory.getByNum(mainCategory, isubCategory) : null,
                addr,
                page,
                iuser,
                prodPerPage
        );

    }

    public List<ProductListVo> getProductListForMain(
            List<Integer> imainCategory,
            List<Integer> isubCategory
    ) {
        int page = 0;
        int limit = Const.MAIN_PROD_PER_PAGE;
        List<ProductListVo> result = new ArrayList<>();
        for (int i = 0; i < imainCategory.size(); i++) {
            result.addAll(getProductList(null, null, imainCategory.get(i), isubCategory.get(i), null, page, limit));
        }


        return result;
    }


    /**
     * 선택한 특정 제품페이지 조회.
     *
     * @param iproduct
     * @return ProductVo
     */
    @CountView(CountCategory.PRODUCT)
    public ProductVo getProduct(int imainCategory, int isubCategory, Long iproduct) {

        Long loginUserPk = authenticationFacade.getLoginUserPk();

        Product findProduct =
                productRepository.findByIproduct(iproduct).orElseThrow(() -> new ClientException(BAD_PRODUCT_INFO_EX_MESSAGE,
                        "해당하는 제품을 찾을 수 없습니다.(iproduct)"));

        if (findProduct.getMainCategory().getNum() != imainCategory || findProduct.getSubCategory().getNum() != isubCategory) {
            throw new ClientException(ErrorCode.ILLEGAL_CATEGORY_EX_MESSAGE, "해당하는 제품을 찾을 수 없습니다.(category)");
        }

        // isLiked
        int isLiked;
        try {
            productLikeRepository.countByIuserAndInIproduct(loginUserPk, List.of(findProduct.getId())).get(0);
            isLiked = 1;
        } catch (IndexOutOfBoundsException ignored) {
            isLiked = 0;
        }

        // Review 가져오기
        List<ReviewResultVo> reviews = getReview(findProduct, 0, IN_PRODUCT_REVIEW_PER_PAGE)
                .stream().map(r -> ReviewResultVo.builder()
                        .ireview(r.getId())
                        .contents(r.getContents())
                        .rating(r.getRating())
                        .iuser(r.getUser().getId())
                        .auth(r.getUser().getAuth().getIauth())
                        .nick(r.getUser().getNick())
                        .userProfPic(r.getUser().getBaseUser().getStoredPic())
                        .build()).toList();

        return ProductVo.builder()
                // user
                .iuser(findProduct.getUser().getId())
                .nick(findProduct.getUser().getNick())
                .userPic(findProduct.getUser().getBaseUser().getStoredPic())
                .iauth(findProduct.getUser().getAuth().getIauth())
                // product
                .iproduct(findProduct.getId())
                .title(findProduct.getTitle())
                .prodMainPic(findProduct.getStoredPic())
                .rentalPrice(findProduct.getRentalPrice())
                .rentalStartDate(findProduct.getRentalDates().getRentalStartDate())
                .rentalEndDate(findProduct.getRentalDates().getRentalEndDate())
                .addr(findProduct.getAddress().getAddr())
                .restAddr(findProduct.getAddress().getRestAddr())
                .view(findProduct.getView())
                .istatus(findProduct.getStatus().getNum())
                .prodLike(findProduct.getProdLikes().size())
                .isLiked(isLiked)
                .hashTags(findProduct.getHashTags().stream().map(hash -> HashTags.builder()
                        .id(hash.getId().intValue())
                        .tag(hash.getTag())
                        .build()).toList())

                .categories(Categories.builder()
                        .mainCategory(findProduct.getMainCategory().getNum())
                        .subCategory(findProduct.getSubCategory().getNum())
                        .build())
                // 리스트와 달리 , 단일 제품 조회시에 추가되는 필드들
                .contents(findProduct.getContents())
                .prodSubPics(findProduct.getPics().stream().map(pic -> PicsInfoVo.builder()
                        .ipics(pic.getId())
                        .prodPics(pic.getStoredPic())
                        .build()).toList())
                .x(findProduct.getAddress().getX())
                .y(findProduct.getAddress().getY())
                .reviews(reviews)
                .build();

    }


    /**
     * 제품 & 제품 사진 등록<br><br>
     * pics + mainPic 개수 검증 - 10개 이하 -> iuser 가 존재하는지 검증 -> category 존재여부 검증 ->
     * price 양수 검증(@V) -> buyDate 오늘보다 이전인지 검증 -> depositPer 70 이상 100 이하 검증(@V) ->
     * 오늘이 rentalStartDate 보다 이전이 아닌지 검증 -> rentalEndDate 가 rentalStartDate 보다 이전이 아닌지 검증
     *
     * @param dto
     * @return ResVo
     */
    @Transactional
    public ResVo postProduct(MultipartFile mainPic, List<MultipartFile> pics, ProductInsDto dto) {

        // 욕설 포함시 예외 발생
        CommonUtils.ifContainsBadWordThrow(BadWordException.class, BAD_WORD_EX_MESSAGE,
                dto.getTitle(), dto.getContents(), dto.getRestAddr());
        // 해시태그 욕설 검증, 예외 발생
        CommonUtils.ifContainsBadWordThrow(BadWordException.class, BAD_WORD_EX_MESSAGE, dto.getHashTags());

        // 메인사진은 필수
        CommonUtils.ifAllNullThrow(BadMainPicException.class, BAD_PIC_EX_MESSAGE, mainPic);

        // 사진 개수 검증 - 예외 코드, 메시지 를 위해 직접 검증 (!@Validated)
        if (pics != null) {
            CommonUtils.checkSizeIfOverLimitNumThrow(ClientException.class,
                    ErrorCode.ILLEGAL_PRODUCT_PICS_EX_MESSAGE, "사진 개수 초과", pics.stream(), 9);
        }

        // 해시태그 개수 검증
        CommonUtils.checkSizeIfOverLimitNumThrow(ClientException.class,
                ErrorCode.ILLEGAL_EX_MESSAGE, "해시태그는 10개까지 가능합니다.", dto.getHashTags().stream(), Const.HASH_TAG_MAX_SIZE);

        // 카테고리 검증 - 예외 코드, 메시지 를 위해 직접 검증 (!@Validated)
        CommonUtils.ifCategoryNotContainsThrow(dto.getIcategory());

        // 날짜 검증 시작  - 예외 코드, 메시지 를 위해 직접 검증 (!@Validated)
        CommonUtils.ifBeforeThrow(
                BadDateInfoException.class, RENTAL_DATE_MUST_BE_BEFORE_THAN_TODAY_EX_MESSAGE,
                dto.getRentalStartDate(), LocalDate.now()
        );
        CommonUtils.ifBeforeThrow(BadDateInfoException.class, RENTAL_END_DATE_MUST_BE_AFTER_THAN_RENTAL_START_DATE_EX_MESSAGE
                , dto.getRentalEndDate(), dto.getRentalStartDate());
        // 날짜 검증 끝


        // logic
        // 주소 검증
        Addrs addrs = axisGenerator.getAxis(dto.getAddr());
        // insert 할 객체 준비 완.
        Long loginUserPk = getLoginUserPk();
        User findUser =
                userRepository.findById(loginUserPk).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                        "로그인 정보가 정확하지 않습니다. 다시 로그인 해주세요."));
        ProductMainCategory mainCategory = ProductMainCategory.getByNum(dto.getIcategory().getMainCategory());

        // 도배방지
        LocalDateTime lastCreatedAt = productRepository.findLastProductCreatedAtBy(findUser);
        if (ChronoUnit.MINUTES.between(lastCreatedAt, LocalDateTime.now()) < 1) {
            throw new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE, "작성글은 1분에 한번만 작성 가능 합니다.");
        }
        Long stockSeq = 0L;

        Product saveProduct = Product.builder()
                .user(findUser)
                .mainCategory(mainCategory)
                .subCategory(ProductSubCategory.getByNum(mainCategory, dto.getIcategory().getSubCategory()))
                .address(Address.builder()
                        .addr(dto.getAddr())
                        .restAddr(dto.getRestAddr())
                        .x(Double.valueOf(addrs.getX()))
                        .y(Double.valueOf(addrs.getY()))
                        .build())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .rentalPrice(dto.getRentalPrice())
                .rentalDates(RentalDates.builder()
                        .rentalStartDate(LocalDateTime.of(dto.getRentalStartDate(), LocalTime.of(0, 0, 0)))
                        .rentalEndDate(LocalDateTime.of(dto.getRentalEndDate(), LocalTime.of(23, 59, 59)))
                        .build())
                .build();
        // 해시태그 저장
        saveProduct.getHashTags().addAll(dto.getHashTags()
                .stream()
                .map(hash -> HashTag.builder()
                        .tag(hash.charAt(0) != '#' ? "#" + hash.replaceAll(" ", "") :
                                hash.replaceAll(" ", ""))
                        .product(saveProduct)
                        .build()).toList());

        // 재고가 여러개면 dto.getStock 해서 재고수만큼 for 문 돌아야 함.
        saveProduct.getStocks().add(Stock.builder()
                .product(saveProduct)
                .seq(++stockSeq)
                .build());

        // 사진은 완전히 따로 저장해야함 (useGeneratedKey) (아직 iproduct 가 없음)

        productRepository.save(saveProduct);
        String iproductString = String.valueOf(saveProduct.getId());

        // 사진 저장
        try {
            saveProduct.setStoredPic(
                    myFileUtils.savePic(mainPic, CATEGORY_PRODUCT_MAIN, String.valueOf(iproductString))
            );
            if (pics != null && !pics.isEmpty()) {

                saveProduct.setPics(
                        myFileUtils.savePic(pics, CATEGORY_PRODUCT_SUB, String.valueOf(iproductString))
                                .stream()
                                .map(picName -> ProdPics.builder()
                                        .product(saveProduct)
                                        .storedPic(picName)
                                        .build()).collect(Collectors.toList())
                );
            }
            return new ResVo(saveProduct.getId());
        } catch (FileNotContainsDotException e) {
            throw new BadMainPicException(BAD_PIC_EX_MESSAGE);
        }
    }


    /**
     * 제품 정보 수정 <br>
     *
     * @param dto
     * @return ResVo
     */
    @Transactional
    public ResVo putProduct(MultipartFile mainPic, List<MultipartFile> pics, ProductUpdDto dto) {

        // 수정할 모든 데이터가 null 이면 예외

        // 병합하지 않아도 되는 데이터 검증
        // 카테고리 검증
        if (dto.getIcategory() != null) {
            CommonUtils.ifCategoryNotContainsThrow(dto.getIcategory());
        }
        CommonUtils.ifAllNullThrow(BadInformationException.class, ALL_INFO_NOT_EXISTS_EX_MESSAGE,
                dto.getHashTags().stream(),
                dto.getIcategory(), dto.getAddr(),
                dto.getRestAddr(), dto.getTitle(),
                dto.getContents(), dto.getRentalPrice(),
                dto.getRentalStartDate(), dto.getRentalEndDate(),
                dto.getDelPics(),
                mainPic, pics);


        CommonUtils.ifContainsBadWordThrow(BadWordException.class, BAD_WORD_EX_MESSAGE,
                dto.getHashTags(),
                dto.getTitle() == null ? "" : dto.getTitle(),
                dto.getContents() == null ? "" : dto.getContents(),
                dto.getRestAddr() == null ? "" : dto.getRestAddr());


        // 작업을 위한 Product 엔티티 가져오기
        Product findProduct =
                productRepository.findByIdFetchUser(dto.getIproduct()).orElseThrow(() -> new ClientException(NO_SUCH_PRODUCT_EX_MESSAGE,
                        "잘못된 제품 정보 (iproduct)"));
        Long loginUserPk = getLoginUserPk();
        if (!Objects.equals(findProduct.getUser().getId(), loginUserPk)) {
            throw new ClientException(ErrorCode.NO_SUCH_USER_EX_MESSAGE, "잘못된 유저정보 (iuser)");
        }


        // 데이터 세팅
        findProduct
                .setChainingRentalPrice(dto.getRentalPrice() == null ? findProduct.getRentalPrice() : dto.getRentalPrice())
                .setChainingRentalDates(dto.getRentalStartDate() == null && dto.getRentalEndDate() == null ?
                        findProduct.getRentalDates() : RentalDates.builder()
                        .rentalStartDate(dto.getRentalStartDate() == null ? findProduct.getRentalDates().getRentalStartDate() :
                                LocalDateTime.of(dto.getRentalStartDate(), LocalTime.of(0, 0, 0)))
                        .rentalEndDate(dto.getRentalEndDate() == null ? findProduct.getRentalDates().getRentalEndDate() :
                                LocalDateTime.of(dto.getRentalEndDate(), LocalTime.of(23, 59, 59)))
                        .build())
                .setChainingContents(dto.getContents() == null ? findProduct.getContents() : dto.getContents())
                .setChainingTitle(dto.getTitle() == null ? findProduct.getTitle() : dto.getTitle())
                .setChainingMainCategory(dto.getIcategory().getMainCategory() == null ? findProduct.getMainCategory() :
                        ProductMainCategory.getByNum(dto.getIcategory().getMainCategory()))
                .setChainingSubCategory(dto.getIcategory().getSubCategory() == null ? findProduct.getSubCategory() :
                        ProductSubCategory.getByNum(ProductMainCategory.getByNum(dto.getIcategory().getMainCategory() == null ?
                                        findProduct.getMainCategory().getNum() : dto.getIcategory().getMainCategory()),
                                dto.getIcategory().getSubCategory()));


        // 데이터 검증
        // 날짜 검증 시작  - 예외 코드, 메시지 를 위해 직접 검증 (!@Validated)
        CommonUtils.ifBeforeThrow(BadDateInfoException.class, RENTAL_END_DATE_MUST_BE_AFTER_THAN_RENTAL_START_DATE_EX_MESSAGE
                , findProduct.getRentalDates().getRentalEndDate().toLocalDate(),
                findProduct.getRentalDates().getRentalStartDate().toLocalDate());
        // 날짜 검증 끝

        // 주소 검증
        // addr 이 제공되었을경우
        if (dto.getAddr() != null) {
            Addrs addrs = axisGenerator.getAxis(dto.getAddr());
            findProduct.setAddress(Address.builder()
                    .addr(dto.getAddr())
                    .x(Double.valueOf(addrs.getX()))
                    .y(Double.valueOf(addrs.getY()))
                    // addr 과 관계없이 restAddr 이 null 이 아니면 해당 값을, null 이면 기존 restAddr 을.
                    .restAddr(dto.getRestAddr() == null ? findProduct.getAddress().getRestAddr() : dto.getRestAddr())
                    .build());
        }

        // addr 은 null 인데, restAddr 은 제공되었을 경우
        // 기존 product 의 addr, x, y 는 그대로 복사하고, restAddr 은 제공된 값으로 설정.
        if (dto.getAddr() == null && dto.getRestAddr() != null) {
            findProduct.setAddress(Address.builder()
                    .addr(findProduct.getAddress().getAddr())
                    .x(findProduct.getAddress().getX())
                    .y(findProduct.getAddress().getY())
                    .restAddr(dto.getRestAddr())
                    .build());
        }

        // 해시태그 개수 검증
        if ((dto.getHashTags().size() - dto.getDelHashTags().size()) + findProduct.getHashTags().size() > 10) {
            throw new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE, "해시태그는 10개까지 가능합니다.");
        }


        // 문제 없음.

        // 해시태그 작업(삭제)
        findProduct.getHashTags().removeAll(
                findProduct.getHashTags().stream().filter(hash -> dto.getDelHashTags().contains(hash.getId())).toList()
        );

        // 해시태그 작업(추가)
        findProduct.getHashTags().addAll(dto.getHashTags().stream().map(hash -> HashTag.builder()
                .tag(hash.charAt(1) != '#' ? "#" + hash.replaceAll(" ", "") : hash.replaceAll(" ", ""))
                .product(findProduct)
                .build()).toList());

        // 삭제사진 필요시 삭제
        // 사진 작업은 최종적으로 되어야함 (오류 발생 가능성을 생각해야함)
        List<ProdPics> delProdPics = null;
        boolean flag = false;

        if (dto.getDelPics() != null && !dto.getDelPics().isEmpty()) {
            delProdPics = findProduct.getPics().stream()
                    .filter(ps -> dto.getDelPics().contains(ps.getId())).toList();
            if (delProdPics.isEmpty()) throw new BadInformationException(BAD_INFO_EX_MESSAGE);

            // use orphanRemoval
            findProduct.getPics().removeAll(delProdPics);
            flag = true;
        }
        if (pics != null && !pics.isEmpty()) {
            try {
                // 추가 사진 insert
                findProduct.getPics().addAll(
                        myFileUtils.savePic(pics, CATEGORY_PRODUCT_SUB, String.valueOf(findProduct.getId()))
                                .stream().map(picName -> ProdPics
                                        .builder()
                                        .product(findProduct)
                                        .storedPic(picName)
                                        .build())
                                .toList()
                );

            } catch (FileNotContainsDotException e) {
                throw new BadMainPicException(BAD_PIC_EX_MESSAGE);
            }
        }
        if (findProduct.getPics().size() > 9) {
            throw new ClientException(ILLEGAL_PRODUCT_PICS_EX_MESSAGE, "사진은 최대 9개까지만 저장가능");
        }

        // 메인사진 수정시
        boolean mainPicFlag = mainPic != null;
        String mainPicPath = null;
        if (mainPicFlag) {
            mainPicPath = findProduct.getStoredPic();

            try {
                findProduct.setStoredPic(myFileUtils.savePic(mainPic, CATEGORY_PRODUCT_MAIN,
                        String.valueOf(dto.getIproduct())));
            } catch (FileNotContainsDotException e) {
                throw new BadMainPicException(BAD_PIC_EX_MESSAGE);
            }

        }

        // 유예된 사진파일 실제로 삭제
        if (mainPicFlag) {
            myFileUtils.delCurPic(mainPicPath);
        }
        if (flag) { // flag 가 true 라는것은 dto.getDelPics() 에 값이 있다는것.
            // 실제 사진 삭제
            delProdPics.stream().map(ProdPics::getStoredPic).forEach(myFileUtils::delCurPic);
        }
        return new ResVo(SUCCESS);
    }


    /**
     * 게시물 삭제<br>
     * 실제로 삭제하지는 않고, 상태를 변경. <br><br><br>
     * 상태설명:<br>
     * 0: 활성화 상태 (제품 insert 시 default 값)<br>
     * -1: 삭제<br>
     * -2: 숨김<br><br><br>
     * <p>
     * 로직:<br>
     * 삭제를 거래중이면 불가능하게? -> 별로인듯 함 그냥 -1 처리 해 두기만 하면 판매자 정보를 join 으로 조회할 수 있음. <br><br>
     * <p>
     * div * -1 로 istatus 값 세팅 <br>
     * -1 -> -1이 아닌곳을 -1 로, <br>
     * -2 -> 0인 곳을 -2 로 <br><br>
     * <p>
     * 결과가 0 이면 -> 잘못된 정보 기입됨. ex 발생.
     *
     * @param iproduct
     * @param div
     * @return ResVo
     */
    @Transactional
    public ResVo delProduct(Long iproduct, Integer div) {

        Product findProduct = productRepository.findByIdAndIuser(iproduct, getLoginUserPk())
                .orElseThrow(() -> new ClientException(BAD_PRODUCT_INFO_EX_MESSAGE,
                        "잘못된 제품 정보 (iproduct or iproduct 가 로그인유저의 것이 아님."));

        findProduct.setStatus(div == 1 ? ProductStatus.DELETED : ProductStatus.HIDDEN);
        productRepository.flush();

        // 삭제시 성공했으니 불필요한 리소스는 모두 지우기
        if (div == 1) {
            List<String> delPicPaths = new ArrayList<>();

            delPicPaths.add(findProduct.getTitle());
            delPicPaths.addAll(findProduct.getPics().stream().map(ProdPics::getStoredPic).toList());
            delPicPaths.forEach(p -> {
                String separator = p.contains("/") ? "/" : p.contains("\\") ? "\\" : p.contains("\\\\") ? "\\\\" : null;
                if (separator == null) return;
                myFileUtils.delFolderTrigger(p.substring(0, p.lastIndexOf(separator)));
            });
        }
        return new ResVo(SUCCESS);
    }

    @Transactional
    public List<ProductUserVo> getUserProductList(Long iuser, Integer page) {
        User findUser = userRepository.findById(iuser == null ? getLoginUserPk() : iuser)
                .orElseThrow(() -> new ClientException(NO_SUCH_USER_EX_MESSAGE, "존재하지 않는 유저입니다."));
        List<ProductStatus> status = new ArrayList<>();
        status.add(ProductStatus.ACTIVATED);
        if (iuser == null) {
            status.add(ProductStatus.HIDDEN);
        }

        List<Product> findProducts = productRepository.findByUser(findUser, page, status);
        List<Long> usersLikeProductInFindProducts = productLikeRepository.findByUserAndProductIn(findUser, findProducts)
                .stream().map(prodLike -> prodLike.getProduct().getId()).toList();


        return findProducts
                .stream()
                .map(product -> ProductUserVo.builder()
                        // user Info
                        .iuser(product.getUser().getId())
                        .nick(product.getUser().getNick())
                        .userPic(product.getUser().getBaseUser().getStoredPic())
                        // product Info
                        .iproduct(product.getId())
                        .title(product.getTitle())
                        .prodMainPic(product.getStoredPic())
                        .rentalPrice(product.getRentalPrice())
                        .rentalStartDate(product.getRentalDates().getRentalStartDate())
                        .rentalEndDate(product.getRentalDates().getRentalEndDate())
                        .addr(product.getAddress().getAddr())
                        .restAddr(product.getAddress().getRestAddr())
                        .view(product.getView())
                        .istatus(product.getStatus().getNum())
                        .prodLike(product.getProdLikes().size())
                        .isLiked(usersLikeProductInFindProducts.contains(product.getId()) ? 1 : 0)
                        .categories(Categories.builder()
                                .mainCategory(product.getMainCategory().getNum())
                                .subCategory(product.getSubCategory().getNum())
                                .build())
                        .icategory(Categories.builder()
                                .mainCategory(product.getMainCategory().getNum())
                                .subCategory(product.getSubCategory().getNum())
                                .build())
                        .build()
                ).collect(Collectors.toList());

    }

    @Transactional
    public List<ReviewResultVo> getAllReviews(Long iproduct, Integer page) {

        return getReview(productRepository.findById(iproduct).orElseThrow(() -> new ClientException(BAD_PRODUCT_INFO_EX_MESSAGE)),
                page,
                TOTAL_REVIEW_PER_PAGE)
                .stream()
                .map(review -> ReviewResultVo.builder()
                        .ireview(review.getId())
                        .contents(review.getContents())
                        .rating(review.getRating())
                        .iuser(review.getUser().getId())
                        .nick(review.getUser().getNick())
                        .userProfPic(review.getUser().getBaseUser().getStoredPic())
                        .status(review.getStatus().getCode())
                        .build())
                .collect(Collectors.toList());
    }

    public List<LocalDate> getDisabledDate(Long iproduct, Integer y, Integer m) {

        return getDisabledDates(
                productRepository.findById(iproduct)
                        .orElseThrow(() -> new ClientException(NO_SUCH_PRODUCT_EX_MESSAGE, "존재하지 않는 상품입니다. (iproduct)")),
                LocalDate.of(y, m, 1));
    }

    /*
        ------- Inner Methods -------
     */

    /**
     * 제품에 포함하여 리뷰를 가져오거나,
     * 해당 제품의 전체 리뷰를 가져올 수 있음.
     * <p>
     * 제품에 포함할때는 page = 1, reviewPerPage 는 Const.inProductReviewPerPage 사용.
     * 전체를 가져올때는 넘어온 page, reviewPerPage 는 Const.totalReviewPerPage 사용.
     *
     * @param product
     * @param page
     * @param reviewPerPage
     * @return List<ReviewResultVo>
     */
    private List<Review> getReview(Product product, Integer page, Integer reviewPerPage) {
        CommonUtils.ifAnyNullThrow(NotEnoughInfoException.class, CAN_NOT_BLANK_EX_MESSAGE,
                product, page, reviewPerPage);

        return productReviewRepository.findByProductOffsetLimit(product, page, reviewPerPage);
    }

    private Long getLoginUserPk() {
        return authenticationFacade.getLoginUserPk();
    }

//    private Integer getDepositFromPerByUtils(ProductUpdDto dto, UpdProdBasicDto fromDb) {
//        return CommonUtils.getDepositFromPer(
//                dto.getPrice() == null ? fromDb.getPrice() : dto.getPrice(),
//                // 가격이나 보증금퍼센트가 변경되면 변경된 보증금 가격으로 바꾸기 위한 로직
//                dto.getDepositPer() == null ?
//                        CommonUtils.getDepositPerFromPrice(dto.getPrice() == null ?
//                                        fromDb.getPrice() :
//                                        dto.getPrice(),
//                                fromDb.getDeposit()) :
//                        dto.getDepositPer());
//    }

    private List<LocalDate> getDisabledDates(Product product, LocalDate refStartDate) {
        LocalDate forRefEndDate = refStartDate.plusMonths(ADD_MONTH_NUM_FOR_DISABLED_DATE);
        return getDisabledDates(product, refStartDate, LocalDate.of(forRefEndDate.getYear(), forRefEndDate.getMonth(),
                forRefEndDate.lengthOfMonth()));
    }

    private List<LocalDate> getDisabledDates(Product product, LocalDate refStartDate, LocalDate refEndDate) {


        // 우선 재고 가져옴
//        final Integer stockCount = refProductRepository.findStockCountBy(iproduct);
        final Integer stockCount = stockRepository.countByProduct(product);

        CommonUtils.ifAnyNullThrow(BadProductInfoException.class, BAD_PRODUCT_INFO_EX_MESSAGE,
                stockCount);

        // 해당 제품의 refStartDate, refEndDate 를 기준으로 겹치는 건이 있는 모든 결제 가져옴.
        List<CanNotRentalDateVo> disabledRefDates =
                productRepository.findCanNotRentalDateVoBy(product, refStartDate, refEndDate);

        // 만약 해당 월들 사이에 이미 거래중인 건이 없다면 곧바로 빈 배열 리턴.
        if (disabledRefDates == null || disabledRefDates.isEmpty()) return new ArrayList<>();

        // 거래 불가능한 날짜들 담을 객체 미리 생성
        List<LocalDate> disabledDates = new ArrayList<>();

        // 검사 시작일부터 하루씩 더해질 객체 생성
        // todo 재고가 1개라면 굳이 일일이 체크할 필요 없다. 하지만, 나중에 재고를 다수로 변경하게 될수도 있으니 이부분까지 커버가 가능하도록 일일이 체크하자.
        // todo (재고가 여러개면 재고만큼 다 중복될수도 있고, 재고중 일부만 거래 불가능할수도 있으니 일일이 체크해야 한다.)
        LocalDate dateWalker = LocalDate.of(refStartDate.getYear(), refStartDate.getMonth(), refStartDate.getDayOfMonth());
        // 작업 시작

        while (!dateWalker.isAfter(refEndDate)) {
            LocalDate lambdaDateWalker = dateWalker;
            if (disabledRefDates.stream().filter(
                    d -> lambdaDateWalker.isEqual(d.getRentalEndDate()) ||
                         lambdaDateWalker.isEqual(d.getRentalStartDate()) ||
                         lambdaDateWalker.isBefore(d.getRentalEndDate()) &&
                         lambdaDateWalker.isAfter(d.getRentalStartDate())
            ).count() >= stockCount) {
                disabledDates.add(LocalDate.of(dateWalker.getYear(),
                        dateWalker.getMonth(),
                        dateWalker.getDayOfMonth()));
            }
            dateWalker = dateWalker.plusDays(ADD_A);
        }
        return disabledDates;
    }

    public ResVo getProdCount(String search, Integer imainCategory, Integer isubCategory, String addr) {

        return new ResVo(productRepository.countBySearchAndMainCategoryAndSubCategory(search, imainCategory, isubCategory, addr));

    }

    public ResVo getUserProductCount(Integer iuser) {
        List<ProductStatus> status = new ArrayList<>();
        status.add(ProductStatus.ACTIVATED);
        if (iuser == null) {
            status.add(ProductStatus.HIDDEN);
        }
        return new ResVo(productRepository.findByIuser(iuser == null || iuser == 0 ? getLoginUserPk() : iuser,
                status));

    }

    public ResVo getReviewCount(Long iproduct) {

        return new ResVo(productRepository.getReviewCount(iproduct));
    }
}
