package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import dtos.PersonRestDTO;
import dtos.PersonsDTO;
import errorhandling.MissingFieldsException;
import errorhandling.PersonNotFoundException;
import facades.PersonFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("person")
public class PersonResource {

    private final EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();

    private final PersonFacade facade = PersonFacade.getPersonFacade(emf);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private UriInfo context;

    public PersonResource() {
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        return Response.ok(gson.toJson(facade.getAllPersons()), MediaType.APPLICATION_JSON).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonById(@PathParam("id") int id) throws PersonNotFoundException {
        PersonDTO pdto = facade.getPerson(id);
        return Response.ok(gson.toJson(pdto), MediaType.APPLICATION_JSON).build();
    }

    @Path("/hobby/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersonsWithHobby(@PathParam("name") String name) {
        PersonsDTO pdto = facade.getAllPersonsWithHobby(name);
        return Response.ok(gson.toJson(pdto), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson (String person) throws MissingFieldsException{
        PersonRestDTO prDTO = gson.fromJson(person, PersonRestDTO.class);
        PersonDTO pDTO = facade.addPerson(prDTO);
        return Response.ok(gson.toJson(pDTO), MediaType.APPLICATION_JSON).build();
    }

    @Path("{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson (@PathParam("id")int id, String person) {
        PersonDTO pDTO = gson.fromJson(person, PersonDTO.class);
        pDTO.setId(id);
        System.out.println(pDTO.toString());
        pDTO = facade.editPerson(pDTO);
        return Response.ok(gson.toJson(pDTO), MediaType.APPLICATION_JSON).build();
    }
}