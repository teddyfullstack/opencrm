package io.nguyenhongphat0.crm.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Lob
    private String information;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Resource avatar;

	@OneToMany(mappedBy = "customer")
    private List<Service> services;

    @OneToMany(mappedBy = "customer")
    private List<Rent> rents;

    public Long getId() {
        return id;
    }

    public Customer() {
        this.createdDate = this.updatedDate = LocalDateTime.now();
    }

    public Customer(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public void setRents(List<Rent> rents) {
        this.rents = rents;
    }

    public Resource getAvatar() {
		return avatar;
	}

	public void setAvatar(Resource avatar) {
		this.avatar = avatar;
	}
}
