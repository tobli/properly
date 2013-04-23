package se.lidskog.maven.properly;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import org.junit.Test;

/**
 * Unit test for ValidatingProperties.
 */
public class ValidatingPropertiesTest
{
	public static final String K1 = "k1";
	public static final String K2 = "k2";
	public static final String K3 = "k3";
	public static final String V1 = "v1";
	public static final String V2 = "v2";
	public static final String V3 = "v3";

	@Test
	public void shouldNotFindDuplicatesInUniqueProperties()
	{
		ValidatingProperties properties = new ValidatingProperties();

		properties.setProperty(K1, V1);
		properties.setProperty(K2, V2);
		properties.setProperty(K3, V3);

		assert properties.stringPropertyNames().size() == 3;
		assert properties.getDuplicatedKeys().isEmpty();
	}

	@Test
	public void shouldFindExactDuplicate()
	{
		ValidatingProperties properties = new ValidatingProperties();

		properties.setProperty(K1, V1);
		properties.setProperty(K2, V2);
		properties.setProperty(K2, V2);

		assert properties.stringPropertyNames().size() == 2;
		assert properties.getDuplicatedKeys().equals(singleton(K2));
		assert properties.getDuplicatedValues(K1).isEmpty();
		assert properties.getDuplicatedValues(K2).equals(singleton(V2));
	}

	@Test
	public void shouldFindOldAndNewValues()
	{
		ValidatingProperties properties = new ValidatingProperties();

		properties.setProperty(K1, V1);
		properties.setProperty(K2, V2);
		properties.setProperty(K2, V3);

		assert properties.stringPropertyNames().size() == 2;
		assert properties.getProperty(K2).equals(V3);
		assert properties.getDuplicatedKeys().equals(singleton(K2));
		assert properties.getDuplicatedValues(K2).equals(new HashSet<String>(asList(V2, V3)));
	}
}
