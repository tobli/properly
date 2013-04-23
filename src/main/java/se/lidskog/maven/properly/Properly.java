package se.lidskog.maven.properly;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.enforcer.AbstractNonCacheableEnforcerRule;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.Scanner;

/**
 *
 */
public class Properly extends AbstractNonCacheableEnforcerRule
{
	private String[] includes = new String[]{ "**/*.properties" };

	@Override
	public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException
	{
		MavenProject project = getMavenProject(helper);

		List<File> propertiesFiles = getPropertiesFiles(project, helper);

		checkForDuplicateProperties(propertiesFiles, helper);

	}

	private List<File> getPropertiesFiles(MavenProject project, EnforcerRuleHelper helper)
	{
		Log log = helper.getLog();

		List<File> files = new ArrayList<File>();

		for (Object o : project.getResources())
		{
			Resource resource = (Resource) o;
			String directory = resource.getDirectory();

			log.info("Scanning; " + directory);
			Scanner scanner = getScannerForDirectory(directory);
			scanner.setIncludes(includes);
			scanner.scan();
			for (String path : scanner.getIncludedFiles())
			{
				files.add(new File(directory, path));
			}
		}

		return files;
	}

	private Scanner getScannerForDirectory(String path)
	{
		DirectoryScanner scanner = new DirectoryScanner();

		scanner.setBasedir(path);

		return scanner;
	}

	public void checkForDuplicateProperties(List<File> propertiesFiles, EnforcerRuleHelper helper) throws EnforcerRuleException
	{
		Log log = helper.getLog();

		List<String> errors = new ArrayList<String>();

		for (File file : propertiesFiles)
		{
			log.info("Checking file: " + file);
			try
			{
				ValidatingProperties validatingProperties = new ValidatingProperties();
				validatingProperties.load(new FileReader(file));

				StringBuilder builder = new StringBuilder();

				for (String key : validatingProperties.getDuplicatedKeys())
				{
					// TODO should preserve order from properties file.
					Set<String> values = validatingProperties.getDuplicatedValues(key);

					builder.append("Duplicate value for key: ").append(key).append("\n\t");
					builder.append("Values were: ").append(Arrays.toString(values.toArray()));
				}

				if (builder.length() > 0)
				{
					builder.insert(0, file.toString() + ": ");
					errors.add(builder.toString());
				}
			}
			catch (IOException e)
			{
				log.error("Error reading properties file: " + file, e);
			}
		}

		if (!errors.isEmpty())
		{
			StringBuilder builder = new StringBuilder("Duplicate properties found!\n");
			for (String error : errors)
			{
				builder.append(error).append("\n");
			}
			throw new EnforcerRuleException(builder.toString());
		}
	}

	/**
	 * Extracted for easier testability.
	 *
	 * @param helper
	 * @return the MavenProject enforcer is running on.
	 *
	 * @throws EnforcerRuleException
	 */
	MavenProject getMavenProject( EnforcerRuleHelper helper ) throws EnforcerRuleException
	{
		try
		{
			return ( MavenProject ) helper.evaluate( "${project}" );
		}
		catch ( ExpressionEvaluationException eee )
		{
			throw new EnforcerRuleException( "Unable to get project.", eee );
		}
	}
}
