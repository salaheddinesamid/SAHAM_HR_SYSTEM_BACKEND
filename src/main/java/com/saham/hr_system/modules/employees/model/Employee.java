package com.saham.hr_system.modules.employees.model;

import com.saham.hr_system.modules.absence.model.Absence;
import com.saham.hr_system.modules.leave.model.Leave;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "employees")
public class Employee implements UserDetails {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "CIN", nullable = false, unique = true)
    private String CIN;

    @Column(name = "family_status")
    @Enumerated(EnumType.STRING)
    private EmployeeFamilyStatus familyStatus;

    @Column(name = "number_of_children")
    private Integer numberOfChildren;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @JoinColumn(name = "balance_id")
    @OneToOne(fetch = FetchType.EAGER)
    private EmployeeBalance employeeBalance;

    @JoinColumn(name = "professional_details_id")
    @OneToOne
    private EmployeeProfessionalDetails employeeProfessionalDetails;

    @JoinColumn(name = "social_details_id")
    @OneToOne
    private EmployeeSocialDetails employeeSocialDetails;

    @JoinColumn(name = "contact_details_id")
    @OneToOne
    private EmployeeContactDetails employeeContactDetails;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_roles",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Leave> leaves = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Absence> absences = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "managed_by")
    private Employee manager;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired()  {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }


    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public String getFullName(){
        return String.format("%s %s", firstName, lastName);
    }
}
