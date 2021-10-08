package facades;

import dtos.PersonDTO;
import dtos.PersonRestDTO;
import dtos.PersonsDTO;

public interface IPersonFacade {
    PersonDTO addPerson(PersonRestDTO pdto) throws Exception;

    PersonDTO deletePerson(Long id) throws Exception;

    PersonDTO getPerson(long id) throws Exception;

    PersonsDTO getAllPersons();

    PersonDTO editPerson(PersonDTO p) throws Exception;
}

