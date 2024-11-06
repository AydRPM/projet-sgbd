package com.mortier.stages.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Inscription", uniqueConstraints = @UniqueConstraint(columnNames = {"enfant_id", "stage_id"}))
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    @NotNull(message = "Le paiement ne peut pas etre vide")
    private Boolean paye;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enfant_id", nullable = false)
    @NotNull(message = "L'enfant ne peut pas etre vide")
    private Enfant enfant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false)
    @NotNull(message = "Le stage ne peut pas etre vide")
    private Stage stage;

    public Inscription() {
    }

    public Inscription(Boolean paye) {
        this.paye = paye;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getPaye() {
        return paye;
    }

    public void setPaye(Boolean paye) {
        this.paye = paye;
    }

    public Enfant getEnfant() {
        return enfant;
    }

    public void setEnfant(Enfant enfant) {
        this.enfant = enfant;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}