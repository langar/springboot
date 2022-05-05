Source : https://marco.dev/spring-boot-batch-tutorial-using-mysql-jpa-and-annotations

##########################################
Le programme suivant, lit les fichiers XML dont le nom est de la forme (situation*.xml) déposés dans (path.to.input.resources = ../DATEXII/datex/)
récupère les données et les insère en bdd avant de déplacer le fichier traité dans (path.to.the.target.work.dir = ../DATEXII/saveDatex/)

##########################################
--
-- Base de données : `springbootbatchjpa`
--

#######################
A lire aussi :

	- EclipseLink XPath 		: https://www.eclipse.org/eclipselink/documentation/2.7/solutions/jpatoxml005.htm
	- EclipseLink JAXB 			: http://blog.bdoughan.com/2011/03/map-to-element-based-on-attribute-value.html
	- Annotation Type XmlPath 	: https://www.eclipse.org/eclipselink/api/2.5/org/eclipse/persistence/oxm/annotations/XmlPath.html
	- Get values from xml file 	: https://stackoverflow.com/questions/8404134/eclipselink-moxy-xmlpath-support-for-axes-parent
	
#######################	
Important :
- le fichier package-info.java, à placer au même endroit que le bean, permet de pallier au problème du namespace
	sinon on ne peut pas marshaller le fichier
- li fichier jaxb.properties avec son contenu fixe, est à placer dans le même package que le bean
	il permet de profiter du jaxb

	#	
Package-info.java : is a way to apply java annotations at the package level

#########################
Pour les namespaces, ajouter 

#####################################
// files that contains the properties, in this case config.properties must be located in the same direcoty of the project
// 	while for application.properties, it must be placed in the project source-folder
@PropertySource(value= {"classpath:application.properties", "file:config.properties"})
public class BatchConfiguration {

Dans le fichier BatchConfiguration, on peut ajouter d'autres fichiers de configuration
With SPRING, you can use @PropertySource annotation to externalize your configuration to a properties file

#########################
i18n
avec spring boot, juste ajouter :
	@Autowired
    MessageSource messageSource;
    
    et récupérer les messages des ressources (messages.properties,..) ainsi :
    messageSource.getMessage("axe.nom", null, Locale.FRENCH)
    
###############################
Excellent : Job & steps
https://docs.spring.io/spring-batch/docs/current/reference/html/step.html

###############################

Pour résoudre un problème JAXB, il aurait fallu ajouter ce qui suit.
Sinon, lors du packaging, dans l'archive on n'aura pas le fichier jaxb.properties dans le même package que le bean

<plugin>
    <artifactId>maven-resources-plugin</artifactId>
    <executions>
        <execution>
            <id>copy-resources</id>
            <phase>validate</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>${basedir}/target/classes/com/fhi/datex/model/</outputDirectory>
                <resources>
                    <resource>
                        <directory>${basedir}/src/main/resources/</directory>
                        <includes>
                            <include>jaxb.properties</include>
                        </includes>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>

