package com.team5.projrental.administration.repository;

import com.team5.projrental.dispute.repository.DisputeBoardRepository;
import com.team5.projrental.entities.Dispute;
import com.team5.projrental.entities.DisputeBoard;
import com.team5.projrental.entities.enums.DisputeStatus;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminDisputeRepositoryTest {


    @Autowired
    AdminDisputeRepository adminDisputeRepository;

    @Autowired
    DisputeBoardRepository disputeBoardRepository;

    private Integer size = -1;

    @Test
    void findByLimitPage() {

        List<Dispute> findDisputes = adminDisputeRepository.findByLimitPage(0, 1, "test", 5, 0);

        for (Dispute findDispute : findDisputes) {
            System.out.println("findDispute.getId() = " + findDispute.getId());
        }
        this.size = findDisputes.size();
        assertThat(findDisputes.size()).isNotEqualTo(0);


    }

    @Test
    void findByIdAndStatus() {

        Dispute findDispute = adminDisputeRepository.findByIdAndStatus(1L, DisputeStatus.STAND_BY)
                .orElseThrow();

        System.out.println("findDispute.getDetails() = " + findDispute.getDetails());

        assertThat(findDispute).isNotNull();
        assertThat(findDispute.getDetails()).isEqualTo("Seatset0");

    }

    @Test
    void totalCountByOptions() {

        Long count = adminDisputeRepository.totalCountByOptions(1, "test", 5, 0);

        System.out.println("count = " + count);

        if (this.size == -1) findByLimitPage();

        assertThat(count.intValue()).isEqualTo(this.size);

    }
}