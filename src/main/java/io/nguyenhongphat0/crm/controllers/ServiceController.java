package io.nguyenhongphat0.crm.controllers;

import io.nguyenhongphat0.crm.entities.Customer;
import io.nguyenhongphat0.crm.entities.Service;
import io.nguyenhongphat0.crm.entities.ServiceItem;
import io.nguyenhongphat0.crm.repositories.ServiceItemRepository;
import io.nguyenhongphat0.crm.repositories.ServiceRepository;
import io.nguyenhongphat0.crm.utils.DateUtil;
import io.nguyenhongphat0.crm.utils.PdfUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    SpringTemplateEngine springTemplateEngine;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    ServiceItemRepository serviceItemRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        return "service/list";
    }

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

    @GetMapping("/{id}/export")
    public void export(@PathVariable long id, HttpServletResponse response) {
        Map<String, Object> model = new HashMap();
        Service service = serviceRepository.findById(id);
        model.put("service", service);
        Date createdDate = Date.from(service.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant());
        String dateHash = new SimpleDateFormat("ddMMyyyy").format(createdDate);
        String dateString = new SimpleDateFormat("MMM d, yyyy").format(createdDate);
        model.put("dateHash", dateHash);
        model.put("dateString", dateString);
        double total = service.getHours() * service.getRate();
        model.put("total", total);
        LocalDateTime monday = service.getCreatedDate().minusDays(6);
        LocalDateTime sunday = service.getCreatedDate();
        String timeRange = monday.format(DateTimeFormatter.ofPattern("MMMM d"))
                + DateUtil.getDayOfMonthSuffix(monday.getDayOfMonth()) + " ("
                + monday.format(DateTimeFormatter.ofPattern("EEEE")) + ") - "
                + sunday.format(DateTimeFormatter.ofPattern("MMMM d"))
                + DateUtil.getDayOfMonthSuffix(sunday.getDayOfMonth()) + " ("
                + sunday.format(DateTimeFormatter.ofPattern("EEEE")) + "):";
        model.put("timeRange", timeRange);
        byte[] data = PdfUtil.generatePdf(springTemplateEngine, "pdf/invoice", model);
        PdfUtil.responsePdf(response, data, service.getName() + ".pdf");
    }
}
