# Properly
Check for errors in Java property files during a Maven build. Implemented as a plugin for Maven's Enforcer plugin.

## How to use
* Clone this repo, build the properly jar and install to you maven repository.
* Add the following snippet to your pom.xml:
<pre>
    &lt;build&gt;
        &lt;plugins&gt;
            &lt;plugin&gt;
                &lt;groupId&gt;org.apache.maven.plugins&lt;/groupId&gt;
                &lt;artifactId&gt;maven-enforcer-plugin&lt;/artifactId&gt;
                &lt;version&gt;1.0&lt;/version&gt;
                &lt;dependencies&gt;
                    &lt;dependency&gt;
                        &lt;groupId&gt;se.lidskog.maven&lt;/groupId&gt;
                        &lt;artifactId&gt;properly&lt;/artifactId&gt;
                        &lt;version&gt;1.0-SNAPSHOT&lt;/version&gt;
                    &lt;/dependency&gt;
                &lt;/dependencies&gt;
                &lt;configuration&gt;
                    &lt;rules&gt;
                        &lt;properly implementation="se.lidskog.maven.properly.Properly"/&gt;
                    &lt;/rules&gt;
                &lt;/configuration&gt;
            &lt;/plugin&gt;
        &lt;/plugins&gt;
    &lt;/build&gt;
</pre>
## Ideas for future improvements
- Ability to configure inclusions/exclusions.
- Work with generated/filtered property files.