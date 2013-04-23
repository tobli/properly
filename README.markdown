# Properly
Check for errors in Java property files during a Maven build. Implemented as a plugin for Maven's Enforcer plugin.

## How to use
* Clone this repo, build the properly jar and install to you maven repository.
* Add the following snippet to your pom.xml:

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-enforcer-plugin</artifactId>
                <version>1.0</version>
                <dependencies>
                    <dependency>
                        <groupId>se.lidskog.maven</groupId>
                        <artifactId>properly</artifactId>
                        <version>1.0-SNAPSHOT</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <rules>
                        <properly implementation="se.lidskog.maven.properly.Properly"/>
                    </rules>
                </configuration>
            </plugin>
        </plugins>
    </build>

## Ideas for future improvements
- Ability to configure inclusions/exclusions.
- Work with generated/filtered property files.