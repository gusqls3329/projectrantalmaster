package com.team5.projrental.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DisputeBoard extends Dispute{



    @BatchSize(size = 1000)
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "iboard")
    private Board board;


}
