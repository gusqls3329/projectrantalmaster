package com.team5.projrental.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Entity
public class DisputeChat extends Dispute {


    @BatchSize(size = 1000)
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "ichat_user")
    private ChatUser chatUser;





}
