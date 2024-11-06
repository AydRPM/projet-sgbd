package com.mortier.stages.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Stage", uniqueConstraints = @UniqueConstraint(columnNames = {"denom"}))
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    @NotEmpty(message = "La denomination ne peut pas etre vide")
    @Size(max = 50, message = "La denomination doit contenir au plus 50 caracteres")
    @Pattern(regexp = "^[A-Z][a-z]{2,}+([[:blank:]]?[a-z]+){0,2}$",
            message = "La denomination doit commencer par une majuscule, suivie d'au moins deux lettres minuscules, " +
                    "contenir au plus trois mots separes par des espaces, sans caracteres speciaux ni accents.")
    private String denom;
    @Column
    @NotNull(message = "L age minimum ne peut pas etre egale a 0")
    @Min(value = 3, message = "L age minimum doit etre superieur ou egale a 3")

    private Integer ageMin;

    @Column
    @NotNull(message = "L age maximum ne peut pas etre egale a 0")
    @Max(value = 18, message = "L age maximum doit etre inferieur ou egale a 18")

    private Integer ageMax;
    @Column
    @FutureOrPresent(message = "La date de debut doit etre dans le futur ou aujourd'hui")
    @NotNull(message = "La date de debut ne peut pas etre vide")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDeb;

    @Column
    @NotNull(message = "La date de fin ne peut pas etre vide")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;

    @Column
    @NotNull(message = "Le prix ne peut pas etre vide")
    @Range(min = 0, max = 1000, message = "Le prix doit etre un nombre entier compris entre 0 et 1000")
    Integer prix;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "stage")
    private List<Inscription> inscriptionsList = new ArrayList<>();

    public Stage() {

    }

    public Stage(String denom, Integer ageMin, Integer ageMax, LocalDate dateDeb, LocalDate dateFin, Integer prix) {
        this.denom = denom;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        this.prix = prix;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDenom() {
        return denom;
    }

    public void setDenom(String denom) {
        this.denom = denom;
    }

    public Integer getPrix() {
        return prix;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public LocalDate getDateDeb() {
        return dateDeb;
    }

    public void setDateDeb(LocalDate dateDeb) {
        this.dateDeb = dateDeb;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    @AssertTrue(message = "L age minimum doit être inferieur a l age maximum")
    public boolean isValidAgeRange() {
        if (ageMin != null && ageMax != null && ageMin > ageMax) {
            return false;
        }
        return true;
    }

    @AssertTrue(message = "La date de debut doit etre egale a la date de fin ou se situer avant la date de fin")
    public boolean isValidDateRange() {
        if (dateDeb != null && dateFin != null && dateDeb.isAfter(dateFin)) {
            return false;
        }
        return true;
    }
}