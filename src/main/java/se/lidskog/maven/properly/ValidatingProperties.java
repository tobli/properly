package se.lidskog.maven.properly;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 *
 */
public class ValidatingProperties extends Properties
{
	private Map<String, Set<String>> duplicates = new HashMap<String, Set<String>>();

	@Override
	public synchronized Object put(Object key, Object value)
	{
		// Properties only allow String keys, so this cast is valid.
		String stringKey = (String) key;
		String previous = (String) super.put(key, value);

		if (previous != null)
		{
			storeDuplicate(stringKey, previous);
			storeDuplicate(stringKey, (String) value);
		}

		return previous;
	}

	public Set<String> getDuplicatedKeys()
	{
		return getUnmodifiableCopy(duplicates.keySet());
	}

	public Set<String> getDuplicatedValues(String key)
	{
		return getUnmodifiableCopy(duplicates.get(key));
	}

	private void storeDuplicate(String key, String value)
	{
		Set<String> set = duplicates.get(key);
		if (set == null)
		{
			set = new HashSet<String>();
		}
		set.add(value);
		duplicates.put(key, set);
	}

	private <T> Set<T> getUnmodifiableCopy(Set<T> set)
	{
		if (set == null)
			return Collections.emptySet();

		return Collections.unmodifiableSet(new HashSet<T>(set));
	}
}
