package io.nguyenhongphat0.crm.controllers;

import io.nguyenhongphat0.crm.entities.Customer;
import io.nguyenhongphat0.crm.repositories.CustomerRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired CustomerRepository customerRepository;

    @GetMapping("/test")
    public String test(@RequestParam String search, Model model) {
        String name = RandomString.make(10);
        Customer customer = new Customer(name);
        customerRepository.save(customer);
        model.addAttribute("customers", customerRepository.findByNameContaining(search));
        return "test";
    }
}
