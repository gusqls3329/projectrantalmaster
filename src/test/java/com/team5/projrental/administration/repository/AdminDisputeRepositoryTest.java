package com.team5.projrental.administration.repository;

import com.team5.projrental.entities.Dispute;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@Slf4j
@DataJpaTest
class AdminDisputeRepositoryTest {


    @Autowired
    AdminDisputeRepository adminDisputeRepository;


    @Test
    void findByLimitPage() {

        Dispute findDispute = adminDisputeRepository.findById(1L).orElseThrow();
        log.info("findDispute : {}", findDispute.getId());

    }

    @Test
    void findByIdAndStatus() {
    }

    @Test
    void totalCountByOptions() {
    }
}