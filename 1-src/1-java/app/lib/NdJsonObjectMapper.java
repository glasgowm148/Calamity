package lib;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NdJsonObjectMapper {

	private static ObjectMapper objectMapper = null;

	/**
	 * Default constructor, which will construct the default {@link JsonFactory}
	 * as necessary, use {@link SerializerProvider} as its
	 * {@link SerializerProvider}, and {@link BeanSerializerFactory} as its
	 * {@link SerializerFactory}. This means that it can serialize all standard
	 * JDK types, as well as regular Java Beans (based on method names and
	 * Jackson-specific annotations), but does not support JAXB annotations.
	 */
	public NdJsonObjectMapper() {
		objectMapper = new ObjectMapper();
	}

	/**
	 * Constructs instance that uses specified {@link JsonFactory} for
	 * constructing necessary {@link JsonParser}s and/or {@link JsonGenerator}s.
	 * 
	 * @param jf
	 *            JsonFactory to use: if null, a new {@link MappingJsonFactory}
	 *            will be constructed
	 */
	public NdJsonObjectMapper(JsonFactory jf) {
		objectMapper = new ObjectMapper(jf);
	}

	/**
	 * Constructs instance that uses specified {@link JsonFactory} for
	 * constructing necessary {@link JsonParser}s and/or {@link JsonGenerator}s,
	 * and uses given providers for accessing serializers and deserializers.
	 * 
	 * @param jf
	 *            JsonFactory to use: if null, a new {@link MappingJsonFactory}
	 *            will be constructed
	 * @param sp
	 *            SerializerProvider to use: if null, a
	 *            {@link SerializerProvider} will be constructed
	 * @param dc
	 *            Blueprint deserialization context instance to use for creating
	 *            actual context objects; if null, will construct standard
	 *            {@link DeserializationContext}
	 */
	public NdJsonObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc) {
		objectMapper = new ObjectMapper(jf, sp, dc);
	}

	/**
	 * Method for registering a module that can extend functionality provided by
	 * this mapper; for example, by adding providers for custom serializers and
	 * deserializers.
	 * 
	 * @param module
	 *            Module to register
	 * 
	 * @return NdJsonObjectMapper
	 */
	public NdJsonObjectMapper registerModule(Module module) {
		objectMapper.registerModule(module);
		return this;
	}

	/**
	 * Convenience method for registering specified modules in order;
	 * functionally equivalent to:
	 * 
	 * <pre>
	 * for (Module module : modules) {
	 * 	registerModule(module);
	 * }
	 * </pre>
	 * 
	 * @param modules
	 *            Modules to register
	 * @return NdJsonObjectMapper
	 */
	public NdJsonObjectMapper registerModules(Module... modules) {
		for (Module module : modules) {
			registerModule(module);
		}
		return this;
	}

	/**
	 * Method to deserialize JSON Stream into Stream of Java type, reference to
	 * which is passed as argument.
	 * 
	 * @param in
	 *            Input Stream
	 * @param valueType
	 *            Class of Object
	 * @param <T>
	 *            The expected class of the Object.
	 * @throws IOException
	 *             if a low-level I/O problem (unexpected end-of-input, network
	 *             error) occurs (passed through as-is without additional
	 *             wrapping
	 * @throws JsonParseException
	 *             if underlying input contains invalid content of type
	 *             {@link JsonParser} supports (JSON for default case)
	 * @throws JsonMappingException
	 *             if the input JSON structure does not match structure expected
	 *             for result type (or has other mismatch issues)
	 * 
	 * @return Stream of Objects
	 */
	public static <T> Stream<T> readValue(InputStream in, Class<T> valueType)
			throws IOException, JsonParseException, JsonMappingException {
		Objects.requireNonNull(in, "InputStream cannot be null");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		try {
			return reader
					.lines()
					.filter(StringUtils::isNotEmpty)
					.map(json -> jsonToObject(json, valueType));
		} catch (NdJsonRunTimeIOException e) {
			throw e.getCause();
		}

	}

	/**
	 * Method to deserialize JSON Stream into List of Java type, reference to
	 * which is passed as argument.
	 * 
	 * @param in
	 *            Input Stream
	 * @param valueType
	 *            Class of Object
	 * @param <T>
	 *            The expected class of the Object.
	 * @throws IOException
	 *             if a low-level I/O problem (unexpected end-of-input, network
	 *             error) occurs (passed through as-is without additional
	 *             wrapping
	 * @throws JsonParseException
	 *             if underlying input contains invalid content of type
	 *             {@link JsonParser} supports (JSON for default case)
	 * @throws JsonMappingException
	 *             if the input JSON structure does not match structure expected
	 *             for result type (or has other mismatch issues)
	 * 
	 * @return List of Objects
	 */
	public <T> List<T> readValueAsList(InputStream in, Class<T> valueType)
			throws IOException, JsonParseException, JsonMappingException {
		return readValue(in, valueType).collect(Collectors.toList());

	}

	/**
	 * Method that can be used to serialize any Java Stream of value as JSON
	 * output, written to OutputStream provided.
	 * 
	 * @param out
	 *            Output Stream
	 * @param value
	 *            Stream of Java Objects
	 * @throws IOException
	 *             if a low-level I/O problem (unexpected end-of-input, network
	 *             error) occurs (passed through as-is without additional
	 *             wrapping
	 * @throws JsonGenerationException
	 *             Exception type for exceptions during JSON writing, such as
	 *             trying to output content in wrong context
	 * @throws JsonMappingException
	 *             if any fatal problems with mapping of content.
	 */
	public void writeValue(OutputStream out, Stream<Object> value)
			throws IOException, JsonGenerationException, JsonMappingException {
		Objects.requireNonNull(out, "OutPutStream cannot be null");
		try {
			value.forEach(val -> objectToJson(out, val));
		} catch (NdJsonRunTimeIOException e) {
			throw e.getCause();
		}
	}

	/**
	 * Method that can be used to serialize any Java List of value as JSON
	 * output, written to OutputStream provided.
	 * 
	 * @param out
	 *            Output Stream
	 * @param value
	 *            List of Java Objects
	 * @throws IOException
	 *             if a low-level I/O problem (unexpected end-of-input, network
	 *             error) occurs (passed through as-is without additional
	 *             wrapping
	 * @throws JsonGenerationException
	 *             Exception type for exceptions during JSON writing, such as
	 *             trying to output content in wrong context
	 * @throws JsonMappingException
	 *             if any fatal problems with mapping of content.
	 */
	public void writeValue(OutputStream out, List<Object> value)
			throws IOException, JsonGenerationException, JsonMappingException {
		Objects.requireNonNull(out, "OutPutStream cannot be null");
		try {
			value.forEach(val -> objectToJson(out, val));
		} catch (NdJsonRunTimeIOException e) {
			throw e.getCause();
		}
	}

	private void objectToJson(OutputStream out, Object val) {
		try {
			objectMapper.writeValue(out, val);
		} catch (IOException e) {
			throw new NdJsonRunTimeIOException(e);
		}
	}

	private static <T> T jsonToObject(String json, Class<T> valueType) {
		try {
			// I added
			//You have created this class or it is builtin ?
			// It's a library, I couldn't import it so I just downloaded
			// I just added this one line, to try and turn the obj into a string
			//there are many other options
			//so you just want json object in string format right ?
			// No, I'm trying to parse the JSON file so that i can do NLP on it
			// i dont know if mapping it to an obj is the right way to do it or if
			//
			// there is an easier way
			// We can just simply ready json and store the results in arrays then in a loop you can apply nlp
			// c
			// cool its ND-Json tho remember, not json
			//ok take me to the code where you are reading json
			//objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true); // https://stackoverflow.com/questions/20837856/can-not-deserialize-instance-of-java-util-arraylist-out-of-start-object-token

			return objectMapper.readValue(json, valueType);
		} catch (IOException e) {
			throw new NdJsonRunTimeIOException(e);
		}
	}

}
