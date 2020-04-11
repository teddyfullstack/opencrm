package io.nguyenhongphat0.crm.controllers;

import io.nguyenhongphat0.crm.entities.Customer;
import io.nguyenhongphat0.crm.entities.Service;
import io.nguyenhongphat0.crm.entities.ServiceItem;
import io.nguyenhongphat0.crm.repositories.ServiceItemRepository;
import io.nguyenhongphat0.crm.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/service")
public class ServiceController {
    @PersistenceContext EntityManager entityManager;
    @Autowired ServiceRepository serviceRepository;
    @Autowired ServiceItemRepository serviceItemRepository;

    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model) {
        Service service = serviceRepository.findById(id);
        model.addAttribute("service", service);
        return "service/detail";
    }

    @PostMapping("/{id}/addItem")
    public RedirectView addItem(@PathVariable long id, ServiceItem item) {
        Service service = entityManager.getReference(Service.class, id);
        item.setService(service);
        serviceItemRepository.save(item);
        return new RedirectView("/service/" + id);
    }

    @GetMapping("/removeItem/{id}")
    public String removeItem(HttpServletRequest request, @PathVariable long id) {
        serviceItemRepository.deleteById(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable long id, Model model) {
        Service service = serviceRepository.findById(id);
        model.addAttribute("service", service);
        return "/service/edit";
    }

    @PostMapping("/{id}/updateInformation")
    public RedirectView updateInformation(@PathVariable long id, Service payload) {
        Service service = serviceRepository.findById(id);
        if (!payload.getName().isEmpty()) {
            service.setName(payload.getName());
        }
        if (payload.getInformation() != null) {
            service.setInformation(payload.getInformation());
        }
        service.setUpdatedDate(LocalDateTime.now());
        serviceRepository.save(service);
        return new RedirectView("/service/" + id);
    }
}
