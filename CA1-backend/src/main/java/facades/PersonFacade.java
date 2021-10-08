package facades;

import dtos.*;
import entities.*;
import errorhandling.PersonNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    private PersonFacade() {
    }

    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    @Override
    public PersonDTO addPerson(PersonRestDTO pdto) {
        EntityManager em = emf.createEntityManager();
        Person person = new Person(pdto.getFirstName(), pdto.getLastName(), pdto.getEmail());
        Address address = new Address(pdto.getAddress().getStreet(), pdto.getAddress().getAdditionalInfo());
        CityInfo cityInfo = em.find(CityInfo.class, pdto.getAddress().getCityInfo().getZipCode());

        try {
            address.addPerson(person);
            cityInfo.addAddress(address);
            em.getTransaction().begin();
            for (PhoneDTO p: pdto.getPhones()) {
                Phone phone = new Phone(p.getNumber(), p.getDescription());
                person.addPhone(phone);
                System.out.println(phone);
            }
            for (Integer i: pdto.getHobbies()) {
                Hobby h = em.find(Hobby.class, (long)i);
                person.addHobby(h);
                System.out.println(h);
            }
            em.persist(cityInfo);
            em.persist(address);
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
            System.out.println(person);
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonDTO deletePerson(Long id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person;
        try {
            em.getTransaction().begin();
            person = em.find(Person.class, id);
            if(person == null) {
                throw new PersonNotFoundException("Could not delete - ID doesn't excist");
            }
            em.remove(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getPerson(long id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, id);
        if (p == null) {
            throw new PersonNotFoundException("No person with given id");
        } else {
            return new PersonDTO(p);
        }
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = query.getResultList();
        return new PersonsDTO(persons);
    }

    @Override
    public PersonDTO editPerson(PersonDTO newPDto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Person tempPerson = em.find(Person.class, newPDto.getId());
            Address tempAddress = em.find(Address.class, newPDto.getAddress().getId());
            CityInfo tempCityInfo = em.find(CityInfo.class, newPDto.getAddress().getCityInfo().getZipCode());

            tempAddress.setStreet(newPDto.getAddress().getStreet());
            tempAddress.setAdditionalInfo(newPDto.getAddress().getAdditionalInfo());

            tempAddress.getCityInfo().removeAddress(tempAddress);


            tempCityInfo.addAddress(tempAddress);

            tempPerson = tempPerson.updateFromDto(newPDto);

            em.merge(tempAddress);
            em.merge(tempPerson);
            em.getTransaction().commit();
            System.out.println(tempPerson);
            return new PersonDTO(tempPerson);
        } finally {
            em.close();
        }
    }

    public PersonDTO getPersonByPhone(String number) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.phones s WHERE s.number = :number", Person.class);
        query.setParameter("number", number);
        List<Person> persons = query.getResultList();
        if (persons.size() == 1) {
            PersonDTO personDTO = new PersonDTO(persons.get(0));
            return personDTO;
        } else {
            return new PersonDTO("fake", "fake", "fake");
        }
    }

    public PersonsDTO getAllPersonsWithHobby(String name) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("select p from Person p join p.hobbies h where h.name = :name", Person.class);
        query.setParameter("name", name);
        List<Person> persons = query.getResultList();
        if (persons != null) {
            PersonsDTO personsDTO = new PersonsDTO(persons);
            return personsDTO;
        } else {
            return new PersonsDTO(persons);
            //TODO: Throw exception
        }
    }

    public PersonsDTO getAllPersonsLivingInCity(String info) {
        EntityManager em = emf.createEntityManager();
        if (info.length() <= 4) {
            TypedQuery<Person> query = em.createQuery("select p from Person p join p.address.cityInfo a where a.zipCode = :info", Person.class);
            query.setParameter("info", info);
            List<Person> persons = query.getResultList();
            if (persons != null) {
                PersonsDTO personsDTO = new PersonsDTO(persons);
                return personsDTO;
            } else {
                return new PersonsDTO(persons);
                //TODO: Throw exception
            }
        } else {
            TypedQuery<Person> query = em.createQuery("select p from Person p join p.address.cityInfo a where a.city = :info", Person.class);
            query.setParameter("info", info);
            List<Person> persons = query.getResultList();
            if (persons != null) {
                PersonsDTO personsDTO = new PersonsDTO(persons);
                return personsDTO;
            } else {
                return new PersonsDTO(persons);
                //TODO: Throw exception
            }
        }
    }

    public PersonsDTO getNumberPeopleFromHobby(String hobby) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("select p from Person p join p.hobbies h where h.name = :hobby", Person.class);
        query.setParameter("hobby", hobby);
        List<Person> persons = query.getResultList();
        if (persons != null) {
            PersonsDTO personsDTO = new PersonsDTO(persons);
            return personsDTO;
        } else {
            return new PersonsDTO(persons);
            //TODO: Throw exception
        }
    }

    public List<CityInfo> getAllZipcodesInDK() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<CityInfo> query = em.createQuery("select c from CityInfo c", CityInfo.class);
        List<CityInfo> cityInfos = query.getResultList();
        List<CityInfo> filteredList = new ArrayList<>();
        for (CityInfo c : cityInfos) {
            if (c.getZipCode().length() == 4) {
                filteredList.add(c);
            } else {
            }
        }
        return filteredList;
    }
}