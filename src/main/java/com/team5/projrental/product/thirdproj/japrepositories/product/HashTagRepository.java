package com.team5.projrental.product.thirdproj.japrepositories.product;

import com.team5.projrental.entities.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long>, HashTagQueryRepository {

}
