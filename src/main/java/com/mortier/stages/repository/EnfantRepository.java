package com.mortier.stages.repository;

import com.mortier.stages.entity.Enfant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnfantRepository extends JpaRepository<Enfant, Integer> {
    public List<Enfant> findByNomStartingWithIgnoreCase(String nom);

    public List<Enfant> findByPrenomStartingWithIgnoreCase(String prenom);

    public List<Enfant> findByNomStartingWithIgnoreCaseAndPrenomStartingWithIgnoreCase(String nom, String prenom);

    public List<Enfant> findByNomAndPrenom(String nom, String prenom);
}