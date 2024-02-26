package com.team5.projrental.entities.mappedsuper;

import com.team5.projrental.entities.embeddable.Address;
import com.team5.projrental.entities.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BaseUser  {

    @Embedded
    private Address address;
    private String storedPic;
    private Double rating;
    private String verification;

}
