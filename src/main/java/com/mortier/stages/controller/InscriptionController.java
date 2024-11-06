package com.mortier.stages.controller;

import com.mortier.stages.entity.Enfant;
import com.mortier.stages.entity.Inscription;
import com.mortier.stages.entity.Stage;
import com.mortier.stages.repository.EnfantRepository;
import com.mortier.stages.repository.InscriptionRepository;
import com.mortier.stages.repository.StageRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Controller

public class InscriptionController {
    @Autowired
    private InscriptionRepository inscriptionRepository;
    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private EnfantRepository enfantRepository;

    private void addModelAttributesEnfantStageAll(Model model) {
        model.addAttribute("listStage", stageRepository.findAll(Sort.by("denom").ascending()));
        model.addAttribute("listEnfant", enfantRepository.findAll(Sort.by("nom").ascending()));
    }

    private void addModelAttributesForResearch(Model model, String denomToSearch, String nomToSearch, Boolean isPaye) {
        boolean showButtonBack = false;

        if (!denomToSearch.isEmpty() && !nomToSearch.isEmpty() && isPaye != null) {
            // Search by denom, nom, and isPaye
            model.addAttribute("listInscription", inscriptionRepository.findByStageDenomStartingWithIgnoreCaseAndEnfantNomStartingWithIgnoreCaseAndPaye(denomToSearch, nomToSearch, isPaye));
            showButtonBack = true;
        } else if (!denomToSearch.isEmpty() && !nomToSearch.isEmpty()) {
            // Search by denom and nom
            model.addAttribute("listInscription", inscriptionRepository.findByStageDenomStartingWithIgnoreCaseAndEnfantNomStartingWithIgnoreCase(denomToSearch, nomToSearch));
            showButtonBack = true;
        } else if (!denomToSearch.isEmpty() && isPaye != null) {
            // Search by denom and isPaye
            model.addAttribute("listInscription", inscriptionRepository.findByStageDenomAndPaye(denomToSearch, isPaye));
            showButtonBack = true;
        } else if (!nomToSearch.isEmpty() && isPaye != null) {
            // Search by nom and isPaye
            model.addAttribute("listInscription", inscriptionRepository.findByEnfantNomStartingWithIgnoreCaseAndPaye(nomToSearch, isPaye));
            showButtonBack = true;
        } else if (!denomToSearch.isEmpty()) {
            // Search only by denom
            model.addAttribute("listInscription", inscriptionRepository.findByStageDenomStartingWithIgnoreCase(denomToSearch));
            showButtonBack = true;
        } else if (!nomToSearch.isEmpty()) {
            // Search only by nom
            model.addAttribute("listInscription", inscriptionRepository.findByEnfantNomStartingWithIgnoreCase(nomToSearch));
            showButtonBack = true;
        } else if (isPaye != null) {
            // Search only by isPaye
            model.addAttribute("listInscription", inscriptionRepository.findByPaye(isPaye));
            showButtonBack = true;
        } else {
            addModelAttributesEnfantStageAll(model);
            model.addAttribute("listInscription", inscriptionRepository.findAll(Sort.by("stage.denom").ascending().and(Sort.by("enfant.nom").ascending())));
        }
        model.addAttribute("showButtonBack", showButtonBack);
    }

    private boolean validAgeRequis(Enfant enfant, Stage stage) {
        LocalDate enfantDateNaiss = enfant.getDateNaiss();
        LocalDate stageDateDeb = stage.getDateDeb();
        if (enfantDateNaiss == null || stageDateDeb == null) {
            return false;
        }
        long ageEnfant = Period.between(enfantDateNaiss, stageDateDeb).getYears();
        return (ageEnfant >= stage.getAgeMin() && ageEnfant <= stage.getAgeMax());
    }

    private Inscription getInscriptionByID(Integer id) {
        Optional<Inscription> optional = inscriptionRepository.findById(id);
        Inscription inscrOpt = null;
        if (optional.isPresent())
            inscrOpt = optional.get();
        else
            throw new RuntimeException("Inscription not found for id: " + id);
        return inscrOpt;
    }

    @GetMapping("/inscriptions")
    public String viewInscriptionsPage(Model model,
                                       @RequestParam(name = "stageInscrToSearch", defaultValue = "") String denomToSearch,
                                       @RequestParam(name = "nomToSearch", defaultValue = "") String nomToSearch,
                                       @RequestParam(name = "isPaye", defaultValue = "") Boolean isPaye) {
        addModelAttributesForResearch(model, denomToSearch, nomToSearch, isPaye);
        return "inscriptions";
    }

    @GetMapping("/showNewInscriptionForm")
    public String newInscriptionForm(Model model) {
        addModelAttributesEnfantStageAll(model);
        Inscription inscription = new Inscription();
        model.addAttribute("inscription", inscription);
        return "new_inscription";
    }

    @PostMapping("/saveNewInscription")
    public String saveNewInscription(@ModelAttribute("inscription") @Valid Inscription inscription, BindingResult result, Model model) {
        addModelAttributesEnfantStageAll(model);
        if (result.hasErrors()) {
            return "new_inscription";
        } else if (!validAgeRequis(inscription.getEnfant(), inscription.getStage())) {
            model.addAttribute("error", inscription.getEnfant().getNom() + " " + inscription.getEnfant().getPrenom()
                    + " (" + inscription.getEnfant().getDateNaiss() + " )"
                    + " n'a pas l'age requis pour le stage de " + inscription.getStage().getDenom() + " : " + inscription.getStage().getAgeMin()
                    + " ans  a " + inscription.getStage().getAgeMax() + " ans");
        } else if (!inscriptionRepository.findByStageAndEnfant(inscription.getStage(), inscription.getEnfant()).isEmpty()) {
            model.addAttribute("error", "Cet enfant est deja inscrit a ce stage");
        } else {
            inscriptionRepository.save(inscription);
            return "redirect:/inscriptions";
        }
        return "new_inscription";
    }

    @GetMapping("/showUpdateInscriptionForm")
    public String showUpdateInscriptionForm(@RequestParam(name = "id") Integer id, Model model) {
        try {
            Inscription inscrOpt = getInscriptionByID(id);
            model.addAttribute("inscription", inscrOpt);
//        Catch RuntimeException si id non trouve (modification de l'url) et redirection pour erreur
        } catch (RuntimeException e) {
            return "redirect:/inscriptions?error=L'inscription+que+vous+essayez+de+mettre+a+jour+n'existe+pas";
        }
        return "update_inscription";
    }

    @PostMapping("/saveUpdateInscription")
    public String saveUpdateInscription(Inscription inscription) {
        inscriptionRepository.save(inscription);
        return "redirect:/inscriptions";
    }

    @GetMapping("/deleteInscription")
    public String deleteInscription(@RequestParam(name = "id") Integer id) {
        inscriptionRepository.deleteById(id);
        return "redirect:/inscriptions";
    }
}