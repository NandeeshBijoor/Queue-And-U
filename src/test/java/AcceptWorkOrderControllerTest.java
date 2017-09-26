import com.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

//import org.springframework.boot.context.embedded
//import org.springframework.boot.test.conte

/**
 * Created by nandeesh.b on 9/25/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptWorkOrderControllerTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();
    private Map<String, Object> addWorkRequestTOQueueRequestMap;
    private Map<String, Object> topWorkRequestTOQueueRequestMap;
    private Map<String, Object> removeWorkRequestTOQueueRequestMap;
    private Map<String, Object> meantimeWorkRequestQueueRequestMap;

    @Before
    public void setUp() throws Exception {
        headers.setContentType(MediaType.APPLICATION_JSON);
        addWorkRequestTOQueueRequestMap = buildRequest("/AddWorkRequestToQueue.json");
        topWorkRequestTOQueueRequestMap = buildRequest("/TopWorkRequestToQueue.json");
        removeWorkRequestTOQueueRequestMap = buildRequest("/RemoveWorkRequestToQueue.json");
        meantimeWorkRequestQueueRequestMap = buildRequest("/MeanTimeWorkRequestQueue.json");

    }

    @After
    public void tearDown() throws Exception{

        HttpEntity<Map> removeEntity = new HttpEntity<Map>(removeWorkRequestTOQueueRequestMap, headers);
        ResponseEntity<String> removeResponse = restTemplate.exchange(
                createURLWithPort("/remove"),
                HttpMethod.POST, removeEntity, String.class);

        HttpEntity<Map> topEntity = new HttpEntity<Map>(null, headers);

        ResponseEntity<String> topResponse = restTemplate.exchange(
                createURLWithPort("/top"),
                HttpMethod.GET, topEntity, String.class);

    }
    @Test
    public void testToAddWorkRequestToQueue() throws Exception {
        HttpEntity<Map> entity = new HttpEntity<Map>(addWorkRequestTOQueueRequestMap, headers);

        ResponseEntity<String> addResponse = restTemplate.exchange(
                createURLWithPort("/add"),
                HttpMethod.POST, entity, String.class);

        String expectedSaveDataResponse = "Work request with request id1 has been added to request Queue on Sun Sep 17 10:44:16 IST 2017";

        //JSONAssert.assertEquals(expectedSaveDataResponse, addResponse.getBody(), false);
        Assert.assertEquals(expectedSaveDataResponse, addResponse.getBody());
    }

    @Test
    public void testToGetTopIDFromWorkRequestQueue() throws Exception {
        HttpEntity<Map> entity = new HttpEntity<Map>(topWorkRequestTOQueueRequestMap, headers);

        ResponseEntity<String> addResponse = restTemplate.exchange(
                createURLWithPort("/add"),
                HttpMethod.POST, entity, String.class);

        HttpEntity<Map> addTopentity = new HttpEntity<Map>(addWorkRequestTOQueueRequestMap, headers);

        ResponseEntity<String> addTopResponse = restTemplate.exchange(
                createURLWithPort("/add"),
                HttpMethod.POST, addTopentity, String.class);


        HttpEntity<Map> topEntity = new HttpEntity<Map>(null, headers);

        ResponseEntity<String> topResponse = restTemplate.exchange(
                createURLWithPort("/top"),
                HttpMethod.GET, topEntity, String.class);

        String expectedSaveDataResponse = "{\"requesterId\":15,\"requestDate\":1505625256000,\"IDClass\":\"Management\"}";

        Assert.assertEquals(expectedSaveDataResponse, topResponse.getBody());


    }

    @Test
    public void listWorkOrderFromWorkRequestQueue() throws Exception {
        HttpEntity<Map> entity = new HttpEntity<Map>(addWorkRequestTOQueueRequestMap, headers);

        ResponseEntity<String> addResponse = restTemplate.exchange(
                createURLWithPort("/add"),
                HttpMethod.POST, entity, String.class);


        HttpEntity<Map> topEntity = new HttpEntity<Map>(null, headers);

        ResponseEntity<String> topResponse = restTemplate.exchange(
                createURLWithPort("/list"),
                HttpMethod.GET, topEntity, String.class);
        //TO-DO - Removed index from list
        String expectedSaveDataResponse = "[{\"requesterId\":1,\"requestDate\":1505625256000,\"IDClass\":\"Normal\"}]";

        Assert.assertEquals(expectedSaveDataResponse, topResponse.getBody().toString());
    }

    @Test
    public void removeWorkOrderFromWorkRequestQueue() throws Exception {
        HttpEntity<Map> entity = new HttpEntity<Map>(addWorkRequestTOQueueRequestMap, headers);

        ResponseEntity<String> addResponse = restTemplate.exchange(
                createURLWithPort("/add"),
                HttpMethod.POST, entity, String.class);


        HttpEntity<Map> removeEntity = new HttpEntity<Map>(removeWorkRequestTOQueueRequestMap, headers);

        ResponseEntity<String> topResponse = restTemplate.exchange(
                createURLWithPort("/remove"),
                HttpMethod.POST, removeEntity, String.class);
        //TO-DO - Removed index from list
        String expectedSaveDataResponse = "15 removed from queue";

        Assert.assertEquals(expectedSaveDataResponse, topResponse.getBody().toString());
    }

    @Test
    public void getPositionFromWorkRequestQueue() throws Exception {

        HttpEntity<Map> entity = new HttpEntity<Map>(topWorkRequestTOQueueRequestMap, headers);

        ResponseEntity<String> addResponse = restTemplate.exchange(
                    createURLWithPort("/add"),
                    HttpMethod.POST, entity, String.class);

        entity = new HttpEntity<Map>(addWorkRequestTOQueueRequestMap, headers);

        addResponse = restTemplate.exchange(
                createURLWithPort("/add"),
                HttpMethod.POST, entity, String.class);

        HttpEntity<Map> removeEntity = new HttpEntity<Map>(removeWorkRequestTOQueueRequestMap, headers);
        ResponseEntity<String> positionResponse = restTemplate.exchange(
                createURLWithPort("/position"),
                HttpMethod.POST, removeEntity, String.class);
        String expectedSaveDataResponse = "0";

        //JSONAssert.assertEquals(expectedSaveDataResponse, addResponse.getBody(), false);
        Assert.assertEquals(expectedSaveDataResponse, positionResponse.getBody().toString());
    }

    @Test
    public void getMeanTimeOfItemsInQueueTest() throws Exception {

        HttpEntity<Map> entity = new HttpEntity<Map>(meantimeWorkRequestQueueRequestMap, headers);

        ResponseEntity<String> meanTimeResponse = restTemplate.exchange(
                createURLWithPort("/meantime"),
                HttpMethod.POST, entity, String.class);

           Assert.assertNotNull(meanTimeResponse.getBody().toString());//TO-DO: assert for actual meantime
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private Map<String, Object> buildRequest(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = this.getClass().getResourceAsStream(fileName);
        try {
            return mapper.readValue(is, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}