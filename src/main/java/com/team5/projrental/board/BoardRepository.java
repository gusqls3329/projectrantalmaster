package com.team5.projrental.board;


import com.team5.projrental.entities.Board;
import com.team5.projrental.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryRepository {

    @Query("select b from Board b join fetch b.user where b.id = :identity")
    Optional<Board> findByIdJoinFetch(Long identity);



    /*@EntityGraph(attributePaths = {"user"})
    List<Board> findAllByUserOrderByIboardDesc(User user, Pageable pageable);*/
}
