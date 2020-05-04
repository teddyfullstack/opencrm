package io.nguyenhongphat0.crm.controllers;

import io.nguyenhongphat0.crm.entities.Customer;
import io.nguyenhongphat0.crm.entities.Estate;
import io.nguyenhongphat0.crm.repositories.EstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        Estate estate = estateRepository.findById(id);
        model.addAttribute("estate", estate);
        return "/estate/edit";
    }

    @PostMapping("/edit/{id}")
    public RedirectView save(@PathVariable long id,  Estate estate) {
        estateRepository.save(estate);
        return new RedirectView("/estate/" + id);
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model) {
        Estate estate = estateRepository.findById(id);
        model.addAttribute("estate", estate);
        return "estate/detail";
    }

    @PostMapping("/delete")
    public RedirectView delete(String id) {
        Long estateId = Long.parseLong(id);
        estateRepository.deleteById(estateId);
        return new RedirectView("/estate");
    }
}
