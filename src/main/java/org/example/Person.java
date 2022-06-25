package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private Sex sex;
    private Integer numberOfChilds;
    private double debt;
    private boolean married;

    private Address address;

    private List<Purchase> purchases = new ArrayList<>();

    public Person() {
    }

    public Person(String name, String surname, LocalDate dateOfBirth, Sex sex, Integer numberOfChilds, double debt, boolean married, Address address, List<Purchase> purchases) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.numberOfChilds = numberOfChilds;
        this.debt = debt;
        this.married = married;
        this.address = address;
        this.purchases = purchases;
    }

    public Person(double debt) {
        this.debt = debt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Integer getNumberOfChilds() {
        return numberOfChilds;
    }

    public void setNumberOfChilds(Integer numberOfChilds) {
        this.numberOfChilds = numberOfChilds;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }
}
