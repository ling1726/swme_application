package ac.at.tuwien.inso.rest;

import ac.at.tuwien.inso.base.AbstractSpringBootTest;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.services.CountryServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

/**
 * Created by ling on 13.06.17.
 */
public class CountryControllerTest extends AbstractSpringBootTest {
    private static final String RESOURCE_BASE_ROUTE = "/countries/";

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    CountryServiceImpl countryService;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void testIndex() throws Exception{
        MvcResult result = mockMvc.perform(get(RESOURCE_BASE_ROUTE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(document("list-countries", responseFields(
                        fieldWithPath("[]").description("list of countries"),
                        fieldWithPath("[].id").description("id of the country"),
                        fieldWithPath("[].name").description("name of the country"),
                        fieldWithPath("[].code").description("iso code of the country")
                )))
                .andReturn();

        JSONArray jsonResult = resultToJSONArray(result);
        assertTrue(jsonResult.length() > 1);

        Country countrySample = countryService.getCountryById(((JSONObject) jsonResult.get(0)).getLong("id"));
        assertNotNull(countrySample);
    }
}
