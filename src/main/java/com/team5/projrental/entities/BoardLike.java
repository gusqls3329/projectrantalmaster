package com.team5.projrental.entities;


import com.team5.projrental.entities.ids.BoardLikeIds;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.entities.mappedsuper.CreatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@DynamicUpdate
@Entity
public class BoardLike extends CreatedAt {
    @EmbeddedId
    private BoardLikeIds boardLikeIds;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @MapsId("iuser")
    @JoinColumn(name = "iuser", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @MapsId("iboard")
    @JoinColumn(nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Board board;
}
