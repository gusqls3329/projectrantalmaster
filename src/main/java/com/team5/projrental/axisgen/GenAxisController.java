package com.team5.projrental.axisgen;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.common.model.restapi.Addrs;
import com.team5.projrental.common.utils.AxisGenerator;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.embeddable.Address;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class GenAxisController {

    private final EntityManager em;
    private final JPAQueryFactory query;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AxisGenerator axisGenerator;

    @Transactional
    @GetMapping("/axisgen/{code}")
    public String axisgen(@PathVariable String code) {
        if (code.length() != 10) {
            return "FALSE";
        }
        log.info("axisgen");

        List<User> findUsers = userRepository.findAll();

        for (User user : findUsers) {
            Addrs findAxis = axisGenerator.getAxis(user.getBaseUser().getAddress().getAddr());
            user.getBaseUser().setAddress(Address.builder()
                    .addr(user.getBaseUser().getAddress().getAddr())
                    .x(Double.valueOf(findAxis.getX()))
                    .y(Double.valueOf(findAxis.getY()))
                    .build());
        }

        productRepository.findAll().forEach(product -> {
            Addrs findAxis = axisGenerator.getAxis(product.getAddress().getAddr());
            product.setAddress(Address.builder()
                    .addr(product.getAddress().getAddr())
                    .x(Double.valueOf(findAxis.getX()))
                    .y(Double.valueOf(findAxis.getY()))
                    .build());
        });

        return "OK";

    }

}
