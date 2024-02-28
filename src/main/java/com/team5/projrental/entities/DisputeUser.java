package com.team5.projrental.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DisputeUser extends Dispute{

    @BatchSize(size = 1000)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ireported_user")
    private User user;


}
