package com.mortier.stages.controller;

import com.mortier.stages.entity.Stage;
import com.mortier.stages.repository.InscriptionRepository;
import com.mortier.stages.repository.StageRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller

public class StageController {
    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    private Stage getStageByID(Integer id) {
        Optional<Stage> optional = stageRepository.findById(id);
        Stage stageOpt = null;
        if (optional.isPresent())
            stageOpt = optional.get();
        else
            throw new RuntimeException("Stage not found for id: " + id);
        return stageOpt;
    }
    private boolean isDenomExists(String denom, Integer currentStageId) {
        List<Stage> stages = stageRepository.findByDenom(denom);
        for (Stage stage : stages) {
            if (!stage.getId().equals(currentStageId)) {
                // Un autre stage avec le même denom existe
                return true;
            }
        }
        return false;
    }
    @GetMapping("/stages")
    public String viewStagesPage(Model model, @RequestParam(name = "nameToSearch", defaultValue = "") String toSearch) {
        model.addAttribute("listStage", stageRepository.findAll(Sort.by("denom").ascending().and(Sort.by("dateDeb").ascending())));
        if (!toSearch.isEmpty()) {
            boolean showButtonBack = true;
            model.addAttribute("showButtonBack", showButtonBack);
            model.addAttribute("listStage", stageRepository.findByDenomStartingWithIgnoreCase(toSearch));
        }
        return "stages";
    }

    @GetMapping("/showNewStageForm")
    public String showNewStageForm(Model model) {
        Stage stage = new Stage();
        model.addAttribute("stage", stage);
        return "new_stage";
    }

    @PostMapping("/saveNewStage")
    public String saveNewStage(@ModelAttribute("stage") @Valid Stage stage, BindingResult result, Model model) {
        model.addAttribute("listStage", stageRepository.findAll());
        if (result.hasErrors()) {
            return "new_stage";
        }
        if (!stageRepository.findByDenom(stage.getDenom()).isEmpty()) {
            model.addAttribute("error", "Le stage " + stage.getDenom() + " existe deja");
            return "new_stage";
        }
        stageRepository.save(stage);
        return "redirect:/stages";
    }

    @GetMapping("/showUpdateStageForm")
    public String showUpdateStageForm(@RequestParam(name = "id") Integer id, Model model) {
        try {
            Stage stageOpt = getStageByID(id);
            model.addAttribute("stage", stageOpt);
//        Catch RuntimeException si id non trouve (modification de l'url) et redirection pour erreur
        } catch (RuntimeException e) {
                return "redirect:/stages?error=Le+stage+que+vous+essayez+de+mettre+a+jour+n'existe+pas";
            }
        return "update_stage";
    }

    @PostMapping("/saveUpdateStage")
    public String saveUpdateStage(@Valid Stage updatedStage, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update_stage";
        }
        Stage existingStage = getStageByID(updatedStage.getId());
        if (!existingStage.getDenom().equals(updatedStage.getDenom()) && isDenomExists(updatedStage.getDenom(), updatedStage.getId())) {
            model.addAttribute("error", "Le stage " + updatedStage.getDenom() + " existe deja.");
            return "update_stage";
        }
        stageRepository.save(updatedStage);
        return "redirect:/stages";
    }

    @GetMapping("/deleteStage")
    public String deleteStage(@RequestParam(name = "id") Integer id, Model model) {
        Stage stageOpt = getStageByID(id);
        if (!inscriptionRepository.findByStage(stageOpt).isEmpty()) {
            return "redirect:/stages?error=Le+stage+ne+peut+pas+etre+supprime+car+il+est+associe+a+des+inscriptions";
        }
        stageRepository.deleteById(id);
        return "redirect:/stages";
    }
}