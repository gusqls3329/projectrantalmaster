package com.team5.projrental.entities;

import com.team5.projrental.entities.mappedsuper.CreatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ichat_user", "seq"}))
public class ChatMsg extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ichat_msg", columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ichatMsg;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "ichat_user")
    private ChatUser chatUser;

    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long seq;




    @Column(length = 2000, nullable = false)
    private String msg;
}
