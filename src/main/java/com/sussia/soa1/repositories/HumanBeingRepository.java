package com.sussia.soa1.repositories;

import com.sussia.soa1.model.HumanBeing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface HumanBeingRepository extends PagingAndSortingRepository<HumanBeing, Long>, JpaSpecificationExecutor<HumanBeing> {
    @Override
    Page<HumanBeing> findAll(Specification<HumanBeing> filter, Pageable pageable);

    @Override
    HumanBeing save(HumanBeing humanBeing);

    @Override
    Optional<HumanBeing> findById(Long id);

    @Override
    void deleteById(Long id);

    long countAllByCar_Cool(Boolean cool);

    long countAllByCar_CoolLessThan(Boolean cool);

    Iterable<HumanBeing> findAllByNameStartsWith(String name);


}
