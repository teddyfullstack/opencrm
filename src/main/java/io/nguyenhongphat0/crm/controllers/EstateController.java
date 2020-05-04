package io.nguyenhongphat0.crm.controllers;

import io.nguyenhongphat0.crm.entities.Estate;
import io.nguyenhongphat0.crm.repositories.EstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/estate")
public class EstateController {
    @Autowired EstateRepository estateRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("estates", estateRepository.findAll());
        return "estate/list";
    }

    @GetMapping("/create")
    public String create() {
        return "estate/edit";
    }

    @PostMapping("/create")
    public RedirectView save(Estate estate) {
        estateRepository.save(estate);
        return new RedirectView("/estate");
    }
}
