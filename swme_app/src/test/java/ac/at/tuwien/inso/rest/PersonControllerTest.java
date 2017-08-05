package ac.at.tuwien.inso.rest;

import ac.at.tuwien.inso.base.AbstractSpringBootTest;
import at.ac.tuwien.inso.entities.Address;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.entities.Person;
import at.ac.tuwien.inso.entities.Title;
import at.ac.tuwien.inso.repositories.interfaces.CountryRepository;
import at.ac.tuwien.inso.repositories.interfaces.PersonRepository;
import at.ac.tuwien.inso.utils.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Adapted from
 * <a href=https://spring.io/guides/tutorials/bookmarks/#_testing_a_rest_service>Testing a java.at.ac.tuwien.inso.java.ac.at.tuwien.inso.rest java.at.ac.tuwien.inso.java.ac.at.tuwien.inso.service</a>
 */
@WebAppConfiguration
public class PersonControllerTest extends AbstractSpringBootTest {

    private static final String RESOURCE_BASE_ROUTE = "/people/";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                  MediaType.APPLICATION_JSON.getSubtype(),
                                                  Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc               mockMvc;
    private HttpMessageConverter  mappingJackson2HttpMessageConverter;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private PersonRepository  personRepository;
    @Autowired
    private CountryRepository countryRepository;

    private Person jane;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();

        jane = new Person();
        jane.setFirstName("Jane");
        jane.setFamilyName("Doe");
        jane.setGender("f");
        jane.setMail("jane@doe.com");
        jane.setBirth(new Date());

        Title title = new Title();
        title.setName("MSc");

        Address address = new Address();
        address.setCity("Nowhere");
        address.setPostCode("12345");
        address.setDoorNumber("5");
        address.setStreet("Janeston Street");
        address.setStreetNumber("10");

        Country country = new Country();
        country.setName("Narnia");
        country.setCode("NA");

        countryRepository.saveAndFlush(country);
        address.setCountry(country);
        jane.setAddress(address);
        jane.setTitle(title);
        personRepository.saveAndFlush(jane);
    }

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                                                         .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                                                         .findAny()
                                                         .orElse(null);

        assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
    }

    @Test
    public void testFilter() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        MvcResult result = mockMvc.perform(get(RESOURCE_BASE_ROUTE + "filterPerson")
                                                   .param("first_name", "Jane")
                                                   .param("family_name", "Doe")
                                                   .param("title", jane.getTitle().getName())
                                                   .param("country", jane.getAddress().getCountry().getName())
                                                   .param("gender", jane.getGender())
                                                    .param("mail",jane.getMail())
                                                    .param("age",jane.getAge())
                                                    .param("birth", dateFormat.format(jane.getBirth()))
                                                    .param("street", jane.getAddress().getStreet())
                                                    .param("streetnum", jane.getAddress().getStreetNumber())
                                                    .param("doornum", jane.getAddress().getDoorNumber())
                                                    .param("province", jane.getAddress().getProvince())
                                                    .param("post", jane.getAddress().getPostCode())
                                                    .param("city", jane.getAddress().getCity()))
                                  .andDo(document("filter-person",
                                          requestParameters(
                                                  parameterWithName("first_name").description("first name of person"),
                                                  parameterWithName("family_name").description("family name of person"),
                                                  parameterWithName("mail").description("email address of person"),
                                                  parameterWithName("birth").description("date of birth of person as yyyy-MM-dd"),
                                                  parameterWithName("title").description("title name as a string of person"),
                                                  parameterWithName("gender").description("gender of person"),
                                                  parameterWithName("age").description("age of person"),
                                                  parameterWithName("streetnum").description("street number of address"),
                                                  parameterWithName("street").description("street name of address"),
                                                  parameterWithName("doornum").description("doorNumber of address"),
                                                  parameterWithName("province").description("province of address"),
                                                  parameterWithName("city").description("city of address"),
                                                  parameterWithName("post").description("postal code of address"),
                                                  parameterWithName("country").description("country name as a string of address")
                                          ),
                                          responseFields(
                                                  fieldWithPath("[]").description("An array of person objects"),
                                                  fieldWithPath("[].id").description("id of the person"),
                                                  fieldWithPath("[].firstName").description("first name of person"),
                                                  fieldWithPath("[].familyName").description("family name of person"),
                                                  fieldWithPath("[].mail").description("email address of person"),
                                                  fieldWithPath("[].birth").description("date of birth of person"),
                                                  fieldWithPath("[].personName").description("full name of the person"),
                                                  fieldWithPath("[].age").description("age of person"),
                                                  fieldWithPath("[].title").description("title of person"),
                                                  fieldWithPath("[].gender").description("gender of person"),
                                                  fieldWithPath("[].address").description("address of person"),
                                                  fieldWithPath("[].address.id").description("id of address"),
                                                  fieldWithPath("[].address.streetNumber").description("street number of address"),
                                                  fieldWithPath("[].address.street").description("street name of address"),
                                                  fieldWithPath("[].address.doorNumber").description("doorNumber of address"),
                                                  fieldWithPath("[].address.province").description("province of address"),
                                                  fieldWithPath("[].address.city").description("city of address"),
                                                  fieldWithPath("[].address.postCode").description("postal code of address"),
                                                  fieldWithPath("[].address.country").description("country of address")
                                          )))
                                  .andExpect(status().isOk())
                                  .andExpect(content().contentType(contentType))
                                  .andReturn();

        JSONArray jsonResult = resultToJSONArray(result);
        assertTrue(jsonResult.length() >= 1);

        Person samplePerson = personRepository.findOne(((JSONObject) jsonResult.get(0)).getLong("id"));
        assertEquals(jane.getFamilyName(), samplePerson.getFamilyName());
        assertEquals(jane.getFirstName(), samplePerson.getFirstName());
        assertNotNull(samplePerson);
    }

    @Test
    public void testFilterTitle() throws Exception {
        MvcResult result = mockMvc.perform(get(RESOURCE_BASE_ROUTE + "filterPerson")
                .param("title", jane.getTitle().getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        JSONArray jsonResult = resultToJSONArray(result);
        assertTrue(jsonResult.length() >= 1);

        for (int i = 0; i < jsonResult.length(); i++) {
            Person samplePerson = personRepository.findOne(((JSONObject) jsonResult.get(i)).getLong("id"));
            if (samplePerson.getTitle() != null) {
                assertEquals(jane.getTitle().getName(), samplePerson.getTitle().getName());
            }
        }

    }


    @Test
    public void testFilterMultiplePeople() throws Exception {
        MvcResult result = mockMvc.perform(get(RESOURCE_BASE_ROUTE + "filterPerson")
                                                   .param("first_name", "Amy"))
                                  .andExpect(status().isOk())
                                  .andExpect(content().contentType(contentType))
                                  .andReturn();

        JSONArray jsonResult = resultToJSONArray(result);
        assertTrue(jsonResult.length() > 1);
    }

    @Test
    public void testFilterPersonNoResults() throws Exception {
        MvcResult result = mockMvc.perform(get(RESOURCE_BASE_ROUTE + "filterPerson")
                                                   .param("first_name", "Sauron"))
                                  .andExpect(status().isOk())
                                  .andExpect(content().contentType(contentType))
                                  .andReturn();

        JSONArray jsonResult = resultToJSONArray(result);
        assertEquals(0, jsonResult.length());
    }

    @Test
    public void testPersonIndex() throws Exception {
        MvcResult result = mockMvc.perform(get(RESOURCE_BASE_ROUTE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(document("index", responseFields(
                        fieldWithPath("[]").description("An array of person objects"),
                        fieldWithPath("[].id").description("id of the person"),
                        fieldWithPath("[].firstName").description("first name of person"),
                        fieldWithPath("[].familyName").description("family name of person"),
                        fieldWithPath("[].mail").description("email address of person"),
                        fieldWithPath("[].birth").description("date of birth of person"),
                        fieldWithPath("[].personName").description("full name of the person"),
                        fieldWithPath("[].age").description("age of person"),
                        fieldWithPath("[].title").description("title of person"),
                        fieldWithPath("[].gender").description("gender of person"),
                        fieldWithPath("[].address").description("address of person"),
                        fieldWithPath("[].address.id").description("id of address"),
                        fieldWithPath("[].address.streetNumber").description("street number of address"),
                        fieldWithPath("[].address.street").description("street name of address"),
                        fieldWithPath("[].address.doorNumber").description("doorNumber of address"),
                        fieldWithPath("[].address.province").description("province of address"),
                        fieldWithPath("[].address.city").description("city of address"),
                        fieldWithPath("[].address.postCode").description("postal code of address"),
                        fieldWithPath("[].address.country").description("country of address")
                        )))
                .andReturn();
        JSONArray jsonResult = resultToJSONArray(result);
        assertTrue(jsonResult.length() > 1);

        Person samplePerson = personRepository.findOne(((JSONObject) jsonResult.get(0)).getLong("id"));
        assertNotNull(samplePerson);
    }

    @Test
    public void testShowPerson() throws Exception {
        Long id = jane.getId();
        assertNotNull(id);

        mockMvc.perform(get(RESOURCE_BASE_ROUTE + "{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.familyName").value("Doe"))
                .andDo(document("get-person",
                        pathParameters(
                                parameterWithName("id").description("the id of the person to fetch")),
                        responseFields(
                                fieldWithPath("id").description("id of the person"),
                                fieldWithPath("firstName").description("first name of person"),
                                fieldWithPath("familyName").description("family name of person"),
                                fieldWithPath("mail").description("email address of person"),
                                fieldWithPath("birth").description("date of birth of person"),
                                fieldWithPath("title").description("title of person"),
                                fieldWithPath("gender").description("gender of person"),
                                fieldWithPath("personName").description("full name of the person"),
                                fieldWithPath("age").description("age of person"),
                                fieldWithPath("address").description("address of person"),
                                fieldWithPath("address.id").description("id of address"),
                                fieldWithPath("address.streetNumber").description("street number of address"),
                                fieldWithPath("address.street").description("street name of address"),
                                fieldWithPath("address.doorNumber").description("doorNumber of address"),
                                fieldWithPath("address.province").description("province of address"),
                                fieldWithPath("address.city").description("city of address"),
                                fieldWithPath("address.postCode").description("postal code of address"),
                                fieldWithPath("address.country").description("country of address")
                        )));
    }

    @Test
    public void testShowNonExistingPerson() throws Exception {
        mockMvc.perform(get(RESOURCE_BASE_ROUTE + 999999L))
               .andExpect(status().isNotFound());
    }


    @Test
    public void testDeletePerson() throws Exception {
        Long id = jane.getId();
        assertNotNull(id);

        mockMvc.perform(delete(RESOURCE_BASE_ROUTE  + "{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.familyName").value("Doe"))
                .andDo(document("delete-person",
                        pathParameters(parameterWithName("id").description("the id of the person to delete")),
                        responseFields(
                                fieldWithPath("id").description("id of the person"),
                                fieldWithPath("firstName").description("first name of person"),
                                fieldWithPath("familyName").description("family name of person"),
                                fieldWithPath("mail").description("email address of person"),
                                fieldWithPath("birth").description("date of birth of person"),
                                fieldWithPath("title").description("title of person"),
                                fieldWithPath("gender").description("gender of person"),
                                fieldWithPath("personName").description("full name of the person"),
                                fieldWithPath("age").description("age of person"),
                                fieldWithPath("address").description("address of person"),
                                fieldWithPath("address.id").description("id of address"),
                                fieldWithPath("address.streetNumber").description("street number of address"),
                                fieldWithPath("address.street").description("street name of address"),
                                fieldWithPath("address.doorNumber").description("doorNumber of address"),
                                fieldWithPath("address.province").description("province of address"),
                                fieldWithPath("address.city").description("city of address"),
                                fieldWithPath("address.postCode").description("postal code of address"),
                                fieldWithPath("address.country").description("country of address")
                        )));

        assertNull(personRepository.findOne(id));
    }

    @Test
    public void testCreatePerson() throws Exception {
        MvcResult result = mockMvc.perform(post(RESOURCE_BASE_ROUTE)
                                                   .contentType(contentType)
                                                   .content(entityToJsonString(jane, mappingJackson2HttpMessageConverter)))
                                  .andExpect(status().isCreated())
                                  .andExpect(content().contentType(contentType))
                                  .andExpect(jsonPath("$.firstName").value("Jane"))
                                  .andExpect(jsonPath("$.familyName").value("Doe"))
                                  .andDo(document("create-person",
                                          requestFields(
                                                  fieldWithPath("id").description("id of the person, can be left as null for creation"),
                                                  fieldWithPath("firstName").description("first name of person"),
                                                  fieldWithPath("familyName").description("family name of person"),
                                                  fieldWithPath("mail").description("email address of person"),
                                                  fieldWithPath("birth").description("date of birth of person"),
                                                  fieldWithPath("title").description("title of person"),
                                                  fieldWithPath("gender").description("gender of person"),
                                                  fieldWithPath("personName").description("full name of the person"),
                                                  fieldWithPath("age").description("age of person"),
                                                  fieldWithPath("address").description("address of person"),
                                                  fieldWithPath("address.id").description("id of address"),
                                                  fieldWithPath("address.streetNumber").description("street number of address"),
                                                  fieldWithPath("address.street").description("street name of address"),
                                                  fieldWithPath("address.doorNumber").description("doorNumber of address"),
                                                  fieldWithPath("address.province").description("province of address"),
                                                  fieldWithPath("address.city").description("city of address"),
                                                  fieldWithPath("address.postCode").description("postal code of address"),
                                                  fieldWithPath("address.country").description("country of address")
                                  ), responseFields(
                                                  fieldWithPath("id").description("id of the person"),
                                                  fieldWithPath("firstName").description("first name of person"),
                                                  fieldWithPath("familyName").description("family name of person"),
                                                  fieldWithPath("mail").description("email address of person"),
                                                  fieldWithPath("birth").description("date of birth of person"),
                                                  fieldWithPath("title").description("title of person"),
                                                  fieldWithPath("gender").description("gender of person"),
                                                  fieldWithPath("personName").description("full name of the person"),
                                                  fieldWithPath("age").description("age of person"),
                                                  fieldWithPath("address").description("address of person"),
                                                  fieldWithPath("address.id").description("id of address"),
                                                  fieldWithPath("address.streetNumber").description("street number of address"),
                                                  fieldWithPath("address.street").description("street name of address"),
                                                  fieldWithPath("address.doorNumber").description("doorNumber of address"),
                                                  fieldWithPath("address.province").description("province of address"),
                                                  fieldWithPath("address.city").description("city of address"),
                                                  fieldWithPath("address.postCode").description("postal code of address"),
                                                  fieldWithPath("address.country").description("country of address")
                                          )
                                  ))
                                  .andReturn();

        JSONObject jsonResult    = resultToJSONObject(result);
        Person     createdPerson = personRepository.findOne(jsonResult.getLong("id"));
        assertNotNull(createdPerson);
    }

    @Test
    public void testUpdatePerson() throws Exception {
        Long id = jane.getId();
        assertNotNull(id);

        Person john = new Person();
        EntityUtils.copyPropertiesIgnoreNull(jane, john);
        john.setId(null);
        john.setPersonName(null);
        john.setFirstName("John");
        john.setGender("m");

        String personJson = entityToJsonString(john, this.mappingJackson2HttpMessageConverter);

        MvcResult result = mockMvc.perform(put(RESOURCE_BASE_ROUTE + id)
                                                   .contentType(contentType)
                                                   .content(personJson))
                                  .andExpect(status().isOk())
                                  .andExpect(content().contentType(contentType))
                                  .andExpect(jsonPath("$.firstName").value("John"))
                                  .andExpect(jsonPath("$.familyName").value("Doe"))
                                  .andExpect(jsonPath("$.gender").value("m"))
                                    .andDo(document("update-person",
                                            requestFields(
                                                    fieldWithPath("id").description("id of the person, can be left as null for creation"),
                                                    fieldWithPath("firstName").description("first name of person"),
                                                    fieldWithPath("familyName").description("family name of person"),
                                                    fieldWithPath("mail").description("email address of person"),
                                                    fieldWithPath("birth").description("date of birth of person"),
                                                    fieldWithPath("title").description("title of person"),
                                                    fieldWithPath("gender").description("gender of person"),
                                                    fieldWithPath("personName").description("full name of the person"),
                                                    fieldWithPath("age").description("age of person"),
                                                    fieldWithPath("address").description("address of person"),
                                                    fieldWithPath("address.id").description("id of address"),
                                                    fieldWithPath("address.streetNumber").description("street number of address"),
                                                    fieldWithPath("address.street").description("street name of address"),
                                                    fieldWithPath("address.doorNumber").description("doorNumber of address"),
                                                    fieldWithPath("address.province").description("province of address"),
                                                    fieldWithPath("address.city").description("city of address"),
                                                    fieldWithPath("address.postCode").description("postal code of address"),
                                                    fieldWithPath("address.country").description("country of address")
                                            ), responseFields(
                                                    fieldWithPath("id").description("id of the person"),
                                                    fieldWithPath("firstName").description("first name of person"),
                                                    fieldWithPath("familyName").description("family name of person"),
                                                    fieldWithPath("mail").description("email address of person"),
                                                    fieldWithPath("birth").description("date of birth of person"),
                                                    fieldWithPath("title").description("title of person"),
                                                    fieldWithPath("gender").description("gender of person"),
                                                    fieldWithPath("personName").description("full name of the person"),
                                                    fieldWithPath("age").description("age of person"),
                                                    fieldWithPath("address").description("address of person"),
                                                    fieldWithPath("address.id").description("id of address"),
                                                    fieldWithPath("address.streetNumber").description("street number of address"),
                                                    fieldWithPath("address.street").description("street name of address"),
                                                    fieldWithPath("address.doorNumber").description("doorNumber of address"),
                                                    fieldWithPath("address.province").description("province of address"),
                                                    fieldWithPath("address.city").description("city of address"),
                                                    fieldWithPath("address.postCode").description("postal code of address"),
                                                    fieldWithPath("address.country").description("country of address")
                                            )
                                    ))
                                  .andReturn();

        JSONObject jsonResult    = resultToJSONObject(result);
        Person     updatedPerson = personRepository.findOne(jsonResult.getLong("id"));
        assertNotNull(updatedPerson);

        // Updated entries should be different
        assertEquals(updatedPerson.getGender(), "m");
        assertEquals(updatedPerson.getFirstName(), "John");
        // Others should stay the same
        assertEquals(updatedPerson.getFamilyName(), jane.getFamilyName());
        assertEquals(updatedPerson.getAddress(), jane.getAddress());
        assertEquals(updatedPerson.getAge(), jane.getAge());
        assertEquals(updatedPerson.getMail(), jane.getMail());
        assertEquals(updatedPerson.getTitle(), jane.getTitle());
        assertEquals(updatedPerson.getPersonName(), john.getFirstName() + " " + jane.getFamilyName());
    }


}
