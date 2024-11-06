package com.mortier.stages.controller;

import com.mortier.stages.entity.Enfant;
import com.mortier.stages.repository.EnfantRepository;
import com.mortier.stages.repository.InscriptionRepository;
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

import java.util.List;
import java.util.Optional;

@Controller

public class EnfantController {
    @Autowired
    private EnfantRepository enfantRepository;
    @Autowired
    private InscriptionRepository inscriptionRepository;

    private Enfant getEnfantByID(Integer id) {
        Optional<Enfant> optional = enfantRepository.findById(id);
        Enfant enfantOpt = null;
        if (optional.isPresent())
            enfantOpt = optional.get();
        else
            throw new RuntimeException("Enfant not found for id: " + id);
        return enfantOpt;
    }

    private boolean isNonAndPrenomExists(String nom, String prenom, Integer currentEnfantId) {
        List<Enfant> enfants = enfantRepository.findByNomAndPrenom(nom, prenom);
        for (Enfant enfant : enfants) {
            if (!enfant.getId().equals(currentEnfantId)) {
                // Un autre enfant avec le même nom et prenom existe
                return true;
            }
        }
        return false;
    }

    private void addModelAttributesForResearch(Model model, String nomToSearch, String prenomToSearch) {
        boolean showButtonBack = false;
        if (!nomToSearch.isEmpty() && !prenomToSearch.isEmpty()) {
            model.addAttribute("listEnfant", enfantRepository.findByNomStartingWithIgnoreCaseAndPrenomStartingWithIgnoreCase(nomToSearch, prenomToSearch));
            showButtonBack = true;
        } else if (!nomToSearch.isEmpty()) {
            model.addAttribute("listEnfant", enfantRepository.findByNomStartingWithIgnoreCase(nomToSearch));
            showButtonBack = true;
        } else if (!prenomToSearch.isEmpty()) {
            model.addAttribute("listEnfant", enfantRepository.findByPrenomStartingWithIgnoreCase(prenomToSearch));
            showButtonBack = true;
        } else {
            model.addAttribute("listEnfant", enfantRepository.findAll(Sort.by("nom").ascending().and(Sort.by("prenom").ascending())));
        }
        model.addAttribute("showButtonBack", showButtonBack);
    }

    @GetMapping("/enfants")
    //DefaultValue evite les erreurs si champs
    public String viewEnfantsPage(Model model,
                                  @RequestParam(name = "nomToSearch", defaultValue = "") String nomToSearch,
                                  @RequestParam(name = "prenomToSearch", defaultValue = "") String prenomToSearch) {
        addModelAttributesForResearch(model, nomToSearch, prenomToSearch);
        return "enfants";
    }

    @GetMapping("/showNewEnfantForm")
    public String showNewEnfantForm(Model model) {
        Enfant enfant = new Enfant();
        model.addAttribute("enfant", enfant);
        return "new_enfant";
    }

    @PostMapping("/saveNewEnfant")
    public String saveNewEnfant(@ModelAttribute("enfant") @Valid Enfant enfant, BindingResult result, Model model) {
        model.addAttribute("listEnfant", enfantRepository.findAll());
        if (result.hasErrors()) {
            return "new_enfant";
        } else if (!enfantRepository.findByNomAndPrenom(enfant.getNom(), enfant.getPrenom()).isEmpty()) {
            model.addAttribute("error", enfant.getNom() + " " + enfant.getPrenom() + " existe deja");
            return "new_enfant";
        } else {
            enfantRepository.save(enfant);
            return "redirect:/enfants";
        }
    }

    @GetMapping("/showUpdateEnfantForm")
    public String showUpdateEnfantForm(@RequestParam(name = "id") Integer id, Model model) {
        try {
            Enfant enfantOpt = getEnfantByID(id);
            model.addAttribute("enfant", enfantOpt);
//        Catch RuntimeException si id non trouve (modification de l'url) et redirection pour erreur
        } catch (RuntimeException e) {
            return "redirect:/enfants?error=L'enfant+que+vous+essayez+de+mettre+a+jour+n'existe+pas";
        }
        return "update_enfant";
    }

    @PostMapping("/saveUpdateEnfant")
    public String saveUpdateEnfant(@Valid Enfant updatedEnfant, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update_enfant";
        }
        Enfant existingEnfant = getEnfantByID(updatedEnfant.getId());
        if (!existingEnfant.getNom().equals(updatedEnfant.getNom()) && isNonAndPrenomExists(updatedEnfant.getNom(), updatedEnfant.getPrenom(), updatedEnfant.getId())) {
            model.addAttribute("error", updatedEnfant.getNom() + " " + updatedEnfant.getPrenom() + " existe deja");
            return "update_enfant";
        }
        enfantRepository.save(updatedEnfant);
        return "redirect:/enfants";
    }

    @GetMapping("/deleteEnfant")
    public String deleteEnfant(@RequestParam(name = "id") Integer id) {
        Enfant enfantOpt = getEnfantByID(id);
        if (!inscriptionRepository.findByEnfant(enfantOpt).isEmpty()) {
            return "redirect:/enfants?error=L'enfant+est+inscrit+a+un+stage+et+ne+peut+donc+pas+etre+supprime";
        }
        enfantRepository.deleteById(id);
        return "redirect:/enfants";
    }
}