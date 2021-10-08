/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.AddressDTO;
import dtos.PersonDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import dtos.PersonRestDTO;
import entities.*;
import errorhandling.PersonNotFoundException;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {

    public static void main(String[] args) throws PersonNotFoundException {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        Person p1 = new Person("Mathias", "Jensen", "cph-mj839@cphbusiness.dk");
        Address a1 = new Address("Holsteinsgade 66", "4.12");
        Phone phone1 = new Phone("20439396", "Cell");
        CityInfo c1 = em.find(CityInfo.class, "2100");
        Hobby h1 = em.find(Hobby.class, (long)500);

        a1.addPerson(p1);
        p1.addPhone(phone1);
        p1.addHobby(h1);
        c1.addAddress(a1);
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(a1);
        em.getTransaction().commit();
        em.close();

    }
}
