package facades;

import dtos.PersonDTO;
import dtos.PersonRestDTO;
import dtos.PersonsDTO;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person person1, person2;
    private static Phone phone1, phone2;
    private static Hobby hobby1, hobby2;
    private static Address address1, address2;
    private static CityInfo ci1, ci2, ci3;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        person1 = new Person("aaa", "bbb", "ccc");
        person2 = new Person("Mathias", "Filtenborg", "F@gmail.com");
        phone1 = new Phone("12345678", "home");
        phone2 = new Phone("87654321", "cell");
        hobby1 = new Hobby("test", "wiki.test", "testing", "tested");
        hobby2 = new Hobby("test1", "wiki.test1", "testing1", "tested1");
        address1 = new Address("Holmgade 20", "4. sal");
        address2 = new Address("Æblegade 6", "Hvidt hus");
        ci1 = new CityInfo("2800", "Kgs. Lyngby");
        ci2 = new CityInfo("2880", "Bagsværd");
        ci3 = new CityInfo("123", "Test");

        person1.addPhone(phone1);
        person1.addHobby(hobby1);
        person2.addPhone(phone2);
        person2.addHobby(hobby1);
        address1.addPerson(person1);
        address2.addPerson(person2);
        ci1.addAddress(address1);
        ci2.addAddress(address2);


        try {
            em.getTransaction().begin();
            em.persist(person1);
            em.persist(person2);
            em.persist(address1);
            em.persist(address2);
            em.persist(ci1);
            em.persist(ci2);
            em.persist(ci3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("delete from phone;").executeUpdate();
        em.createNativeQuery("delete from hobby_person;").executeUpdate();
        em.createNativeQuery("delete from hobby;").executeUpdate();
        em.createNativeQuery("delete from person;").executeUpdate();
        em.createNativeQuery("delete from address;").executeUpdate();
        em.createNativeQuery("delete from cityinfo;").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void addPersonTest() {
        Person tempPerson = person2;
        tempPerson.setId(person2.getId());
        PersonDTO expResult = new PersonDTO(tempPerson);
        String fName = "Mathias";
        String lName = "Filtenborg";
        String email = "F@gmail.com";
        Person person = new Person(fName, lName, email);
        person.setId(person2.getId());
        Address address = new Address("Æblegade 6", "Hvidt hus");
        CityInfo cityInfo = new CityInfo("2880", "Bagsværd");
        Phone phone = new Phone("87654321", "cell");
        Hobby hobby = new Hobby("test", "wiki.test", "testing", "tested");
        person.addPhone(phone);
        person.addHobby(hobby);
        address.addPerson(person);
        cityInfo.addAddress(address);

        PersonDTO result = facade.addPerson(new PersonRestDTO(person));
        assertEquals(expResult.getFirstName(), result.getFirstName());
        assertEquals(expResult.getLastName(), result.getLastName());
        assertEquals(expResult.getEmail(), result.getEmail());
    }

    //TODO: Virker men nok ikke den rigtige måde
    @Test
    public void deletePersonTest() throws Exception {
        facade.deletePerson(person1.getId());
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Person p = em.find(Person.class, person1.getId());
        em.getTransaction().commit();
        em.close();
        Assertions.assertNull(p);
    }

    @Test
    public void getPersonTest() throws Exception {
        PersonDTO expResult = new PersonDTO(person1);
        PersonDTO result = facade.getPerson(person1.getId());
        assertEquals(expResult, result);
    }

    @Test
    public void getAllPersons() {
        PersonsDTO persons = facade.getAllPersons();
        assertEquals(persons.getAll().size(),2);
    }

    @Test
    public void editPersonTest() {
        PersonDTO actPDto = new PersonDTO(person2);
        PersonDTO newPDto = new PersonDTO("August", "filtenborg", "f@gmail.com");
        newPDto.setId(person2.getId());
        actPDto = facade.editPerson(newPDto);
        assertEquals(actPDto.getFirstName(), newPDto.getFirstName());
    }

    @Test
    public void getPersonByPhoneTest() {
        Person person = person2;
        PersonDTO result = facade.getPersonByPhone("87654321");
        person.updateFromDto(result);

        assertEquals("Mathias", person.getFirstName());
        assertEquals("test", person.getHobbies().get(0).getName());
        assertEquals("Æblegade 6", person.getAddress().getStreet());
        assertEquals("2880", person.getAddress().getCityInfo().getZipCode());
    }

    @Test
    public void getAllPersonsWithHobbyTest() {
        PersonsDTO result = facade.getAllPersonsWithHobby("test");
        PersonDTO p1DTO = new PersonDTO(person1);
        PersonDTO p2DTO = new PersonDTO(person2);
        assertThat(result.getAll(), containsInAnyOrder(p1DTO, p2DTO));
    }

    @Test
    public void getAllPersonsLivingInCityTest() {
        PersonsDTO result = facade.getAllPersonsLivingInCity("2800");
        assertEquals(1, result.getAll().size());
        assertEquals("aaa", result.getAll().get(0).getFirstName());
    }

    @Test
    public void getNumberPeopleFromHobbyTest() {
        PersonsDTO result = facade.getNumberPeopleFromHobby("test");
        assertEquals(2, result.getAll().size());
    }

    @Test
    public void getAllZipcodesInDKTest() {
        List<CityInfo> result = facade.getAllZipcodesInDK();
        assertEquals(2, result.size());
    }
}