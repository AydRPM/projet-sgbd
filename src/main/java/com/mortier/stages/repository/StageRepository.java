package com.mortier.stages.repository;

import com.mortier.stages.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageRepository extends JpaRepository<Stage, Integer> {
    public List<Stage> findByDenomStartingWithIgnoreCase(String denom);

    public List<Stage> findByDenom(String denom);
}