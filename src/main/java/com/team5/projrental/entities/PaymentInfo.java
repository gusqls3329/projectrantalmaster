package com.team5.projrental.entities;

import com.team5.projrental.entities.enums.PaymentInfoStatus;
import com.team5.projrental.entities.ids.PaymentInfoIds;
import com.team5.projrental.entities.mappedsuper.BaseAt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PaymentInfo extends BaseAt {

    @EmbeddedId
    private PaymentInfoIds paymentInfoIds;

    //

    @MapsId("ipayment")
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "ipayment")
    private Payment payment;

    @MapsId("iuser")
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iuser")
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVATED'")
    private PaymentInfoStatus status = PaymentInfoStatus.ACTIVATED;
    private String code;


}
