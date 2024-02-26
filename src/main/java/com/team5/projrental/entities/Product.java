package com.team5.projrental.entities;

import com.team5.projrental.entities.embeddable.Address;
import com.team5.projrental.entities.embeddable.RentalDates;
import com.team5.projrental.entities.enums.ProductMainCategory;
import com.team5.projrental.entities.enums.ProductStatus;
import com.team5.projrental.entities.enums.ProductSubCategory;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.entities.mappedsuper.BaseAt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product extends BaseAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iproduct")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iuser")
    private User user;

    @Enumerated(EnumType.STRING)
    private ProductMainCategory mainCategory;
    @Enumerated(EnumType.STRING)
    private ProductSubCategory subCategory;

    @Embedded
    private Address address;

    @Builder.Default
    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "product")
    private List<ProdLike> prodLikes = new ArrayList<>();

    private String title;
    private String contents;
    private String storedPic;
    private Integer rentalPrice;

    @Embedded
    private RentalDates rentalDates;

    @Builder.Default
    @ColumnDefault("0")
    private Long view = 0L;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVATED'")
    private ProductStatus status = ProductStatus.ACTIVATED;

    @Builder.Default
    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ProdPics> pics = new ArrayList<>();

    @Builder.Default
    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Stock> stocks = new ArrayList<>();

    @Builder.Default
    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<HashTag> hashTags = new ArrayList<>();





    public Product setChainingId(Long id) {
        this.id = id;
        return this;
    }

    public Product setChainingUser(User user) {
        this.user = user;
        return this;
    }

    public Product setChainingMainCategory(ProductMainCategory mainCategory) {
        this.mainCategory = mainCategory;
        return this;
    }

    public Product setChainingSubCategory(ProductSubCategory subCategory) {
        this.subCategory = subCategory;
        return this;
    }

    public Product setChainingAddress(Address address) {
        this.address = address;
        return this;
    }

    public Product setChainingProdLikes(List<ProdLike> prodLikes) {
        this.prodLikes = prodLikes;
        return this;
    }

    public Product setChainingTitle(String title) {
        this.title = title;
        return this;
    }

    public Product setChainingContents(String contents) {
        this.contents = contents;
        return this;
    }

    public Product setChainingStoredPic(String storedPic) {
        this.storedPic = storedPic;
        return this;
    }

    public Product setChainingRentalPrice(Integer rentalPrice) {
        this.rentalPrice = rentalPrice;
        return this;
    }

    public Product setChainingRentalDates(RentalDates rentalDates) {
        this.rentalDates = rentalDates;
        return this;
    }

    public Product setChainingView(Long view) {
        this.view = view;
        return this;
    }

    public Product setChainingStatus(ProductStatus status) {
        this.status = status;
        return this;
    }

    public Product setChainingPics(List<ProdPics> pics) {
        this.pics = pics;
        return this;
    }
}
