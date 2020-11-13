package logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Tweet;

import java.io.IOException;
import java.util.List;

public class JacksonExample {

    public JacksonExample(String json) {

        ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        //String json = "[{\"name\":\"mkyong\", \"age\":37}, {\"name\":\"fong\", \"age\":38}]";

        try {

            // 1. convert JSON array to Array objects
            /**
            Tweet[] pp1 = mapper.readValue(json, Tweet[].class);

            System.out.println("JSON array to Array objects...");
            for (Tweet tweet : pp1) {
                System.out.println(tweet);
            }



            // 2. convert JSON array to List of objects
            //List<Tweet> ppl2 = Arrays.asList(mapper.readValue(json, Tweet[].class));

            //System.out.println("\nJSON array to List of objects");
            //ppl2.stream().forEach(x -> System.out.println(x.getText()));
            **/

            // 3. alternative
            List<Tweet> pp3 = mapper.readValue(json, new TypeReference<List<Tweet>>() {});

            System.out.println("\nAlternative...");
            pp3.stream().forEach(x -> System.out.println(x));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}