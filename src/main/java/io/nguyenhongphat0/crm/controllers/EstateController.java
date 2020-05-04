package io.nguyenhongphat0.crm.controllers;

import io.nguyenhongphat0.crm.entities.Estate;
import io.nguyenhongphat0.crm.entities.Resource;
import io.nguyenhongphat0.crm.repositories.EstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Base64;

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

    @PostMapping("/{id}/uploadPictures")
    public RedirectView uploadPictures(@PathVariable long id, @RequestParam MultipartFile[] pictures) throws IOException {
        Estate estate = estateRepository.findById(id);
        for (MultipartFile picture: pictures) {
            String base64 = Base64.getEncoder().encodeToString(picture.getBytes());
            Resource resource = new Resource(base64);
            estate.getPictures().add(resource);
        }
        estateRepository.save(estate);
        return new RedirectView("/estate/" + id);
    }

    @RequestMapping("/{id}/clearPictures")
    public RedirectView clearPictures(@PathVariable long id) {
        Estate estate = estateRepository.findById(id);
        estate.getPictures().clear();
        estateRepository.save(estate);
        return new RedirectView("/estate/" + id);
    }
}
