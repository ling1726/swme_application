package ac.at.tuwien.inso.base;

import at.ac.tuwien.inso.StandaloneApplication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = StandaloneApplication.class)
public abstract class AbstractSpringBootTest {

    protected String entityToJsonString(Object o, HttpMessageConverter converter) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        converter.write(o, MediaType.APPLICATION_JSON,
                mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    protected JSONObject resultToJSONObject(MvcResult result) throws UnsupportedEncodingException {
        String contentAsString = result.getResponse().getContentAsString();
        return new JSONObject(contentAsString);
    }

    protected JSONArray resultToJSONArray(MvcResult result) throws UnsupportedEncodingException {
        String contentAsString = result.getResponse().getContentAsString();
        return new JSONArray(contentAsString);
    }
    
}
