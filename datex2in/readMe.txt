Source : https://marco.dev/spring-boot-batch-tutorial-using-mysql-jpa-and-annotations



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
- le fichier jaxb.properties avec son contenu fixe, est à placer dans le même package que le bean
	il permet de profiter du jaxb

	#	
Package-info.java : is a way to apply java annotations at the package level

#########################
Pour les namespaces, ajouter 

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