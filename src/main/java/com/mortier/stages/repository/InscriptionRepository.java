package com.mortier.stages.repository;

import com.mortier.stages.entity.Enfant;
import com.mortier.stages.entity.Inscription;
import com.mortier.stages.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {
    public List<Inscription> findByStageDenomStartingWithIgnoreCase(String denom);

    public List<Inscription> findByPaye(Boolean paye);

    @Query("select s from Inscription s where  s.paye = ?2 AND UPPER(s.stage.denom) LIKE UPPER(CONCAT(?1, '%'))")
    List<Inscription> findByStageDenomAndPaye(String stageInscrName, Boolean paye);

    List<Inscription> findByStageAndEnfant(Stage stage, Enfant enfant);

    List<Inscription> findByStage(Stage stage);

    List<Inscription> findByEnfant(Enfant enfant);

    List<Inscription> findByStageDenomStartingWithIgnoreCaseAndEnfantNomStartingWithIgnoreCase(String stageInscrName, String enfantInscrName);

    List<Inscription> findByStageDenomStartingWithIgnoreCaseAndEnfantNomStartingWithIgnoreCaseAndPaye(String stageInscrName, String enfantInscrName, Boolean paye);

    List<Inscription> findByEnfantNomStartingWithIgnoreCaseAndPaye(String enfantInscrName, Boolean paye);

    List<Inscription> findByEnfantNomStartingWithIgnoreCase(String enfantInscrName);
}