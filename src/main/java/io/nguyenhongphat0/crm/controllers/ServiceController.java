package io.nguyenhongphat0.crm.controllers;

import io.nguyenhongphat0.crm.entities.Service;
import io.nguyenhongphat0.crm.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Controller
@RequestMapping("/service")
public class ServiceController {
    @Autowired ServiceRepository serviceRepository;


}
