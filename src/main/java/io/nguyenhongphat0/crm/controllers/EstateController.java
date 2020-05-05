package io.nguyenhongphat0.crm.controllers;

import io.nguyenhongphat0.crm.entities.*;
import io.nguyenhongphat0.crm.repositories.CustomerRepository;
import io.nguyenhongphat0.crm.repositories.EstateRepository;
import io.nguyenhongphat0.crm.repositories.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/estate")
public class EstateController {
    @PersistenceContext EntityManager entityManager;
    @Autowired EstateRepository estateRepository;
    @Autowired CustomerRepository customerRepository;
    @Autowired RentRepository rentRepository;

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
        model.addAttribute("now", LocalDate.now());
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

    @GetMapping("/{id}/assign")
    public String assign(@PathVariable long id, Model model) {
        model.addAttribute("estate", estateRepository.findById(id));
        model.addAttribute("customers", customerRepository.findAll());
        return "/estate/assign";
    }

    @PostMapping("{id}/assign")
    public RedirectView assign(@PathVariable long id, long[] customers, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentDate) {
        if (rentDate == null) {
            rentDate = LocalDate.now();
        }
        Estate estate = estateRepository.findById(id);
        List<Rent> currentRents = rentRepository.findRentsByEstateIdAndEndDateIsNull(id);
        for (Rent rent : currentRents) {
            rent.setEndDate(rentDate);
        }
        rentRepository.saveAll(currentRents);
        for (long customerId : customers) {
            Rent rent = new Rent();
            Customer customer = entityManager.getReference(Customer.class, customerId);
            rent.setCustomer(customer);
            rent.setEstate(estate);
            rent.setStartDate(rentDate);
            rent.setPrice(estate.getPrice() / customers.length);
            rentRepository.save(rent);
        }
        return new RedirectView("/estate/" + id);
    }

    @PostMapping("/pay")
    public RedirectView pay(HttpServletRequest request, long rentId, double amount) {
        Rent rent = rentRepository.findById(rentId);
        rent.setPaid(rent.getPaid() + amount);
        rentRepository.save(rent);
        String referer = request.getHeader("Referer");
        return new RedirectView(referer);
    }
}
