package com.mortier.stages.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Enfant")
public class Enfant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    @NotEmpty(message = "Le prenom ne peut pas etre vide")
    @Size(max = 100, message = "Le prenom doit contenir au plus 100 caracteres")
    @Pattern(regexp = "^[A-Z][a-z]+(-[A-Z][a-z]+)?( [A-Z][a-z]+(-[A-Z][a-z]+)?){0,2}$",
            message = "Le prenom doit commencer par une majuscule, suivie d au moins une lettre minuscule, " +
                    "contenir au plus trois mots simples ou composes separes par des espaces, sans caracteres speciaux ni accents (hors - pour les compositions)")
    private String prenom;
    @Column
    @NotEmpty(message = "Le nom ne peut pas etre vide")
    @Size(max = 100, message = "Le nom doit contenir au plus 100 caracteres")
    @Pattern(regexp = "^[A-Z][a-z]+(-[A-Z][a-z]+)?( [A-Z][a-z]+(-[A-Z][a-z]+)?){0,2}$", message = "Le nom doit commencer par une majuscule, suivie d au moins une lettre minuscule, " +
            "contenir au plus trois mots simple ou composes separes par des espaces, sans caracteres speciaux ni accents (hors - pour les compositions)")
    private String nom;
    @Enumerated(EnumType.STRING)
    @Column(name = "Sexe")
    private Gender gender;
    @Column
    @Past(message = "La date de naissance doit etre dans le passe")
    @NotNull(message = "La date de naissance ne peut pas etre vide")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNaiss;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "enfant")
    private List<Inscription> inscriptions = new ArrayList<>();

    public Enfant() {
    }

    public Enfant(String prenom, String nom, Gender gender, LocalDate dateNaiss) {
        this.prenom = prenom;
        this.nom = nom;
        this.gender = gender;
        this.dateNaiss = dateNaiss;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Gender getSexe() {
        return gender;
    }

    public void setSexe(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(LocalDate dateNaiss) {
        this.dateNaiss = dateNaiss;
    }
}