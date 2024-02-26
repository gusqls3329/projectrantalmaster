package com.team5.projrental.entities;

import com.team5.projrental.entities.mappedsuper.CreatedAt;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ProdPics extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ipics")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "iproduct")
    private Product product;

    private String storedPic;


}
