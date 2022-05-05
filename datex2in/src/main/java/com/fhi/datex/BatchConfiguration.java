package com.fhi.datex;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.fhi.datex.model.Situation;
import com.fhi.datex.processor.SituationItemProcessor;
import com.fhi.datex.utils.Constantes;


@Configuration
@EnableBatchProcessing
@ComponentScan
//spring boot configuration
@EnableAutoConfiguration
// files that contains the properties, in this case config.properties must be located in the same direcoty of the project
// 	while for application.properties, it must be placed in the project source-folder
@PropertySource(value= {"classpath:application.properties", "file:config.properties"})
public class BatchConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
	
    /*
        Load the properties
     */
    @Value("${database.driver}")
    private String databaseDriver;
    @Value("${database.url}")
    private String databaseUrl;
    @Value("${database.username}")
    private String databaseUsername;
    @Value("${database.password}")
    private String databasePassword;

    
    @Value("${path.to.input.resources}")
    private String inputResources;
        
    @Value("${path.to.input.resources.login}")
    private String resourcesLogin;
    
    @Value("${path.to.input.resources.pwd}")
    private String resourcesPassword;
    
    @Value("${path.to.the.target.work.dir}")
	private String targetResources;
    
    @Value("${files.extensions}")
    private String extensionsFiles;
    
    
    @Autowired
    JobBuilderFactory jobs;
    
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    
    /**
     * 
     * @return
     */
    @Bean
    public Job importSituationJob() {
    	 logger.debug("==========	importSituationJob =========");
        return jobs.get("importSituationJob")
                .incrementer(new RunIdIncrementer()) // because a spring config bug, this incrementer is not really useful
                .start(situationLoadStep())
                .on("FAILED").end()
                .from(situationLoadStep()).on("*").to(moveFileStep())
                .end()
                .build();
    }
    
    /**
     * Step
     * We declare that every 1000 lines processed the data has to be committed
     * @return
     */
    @Bean
    public Step situationLoadStep() {
    	logger.debug("==========	step1 : situationLoad =========");
    	return stepBuilderFactory.get("situationLoadStep")
                .<Situation, Situation>chunk(1000)
                .reader(multiItemReader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    

    
    /**
     * 
     * @return
     */
    @Bean
    @StepScope
    public MultiResourceItemReader<Situation> multiItemReader() {
      ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
      org.springframework.core.io.Resource[] resources = null;

      try {
   		  resources = patternResolver.getResources(Constantes.PREFIXE + inputResources + extensionsFiles);
      } catch (Exception e) {
        logger.error("error reading files", e);
      }
      
      logger.debug("Number of files to read [" + Constantes.PREFIXE + inputResources + extensionsFiles + "]" + resources.length); 
      return new MultiResourceItemReaderBuilder<Situation>()
          .name("multiItemReader").delegate(reader())
          .resources(resources)
          .setStrict(false)
          .build();
    }
    
	/**
     * We define a bean that read each line of the input file.
     *
     * @return
     */
    @Bean
    public StaxEventItemReader<Situation> reader() {
    	
    	Jaxb2Marshaller situationMarshaller = new Jaxb2Marshaller();
        situationMarshaller.setClassesToBeBound(Situation.class);
        logger.debug("==========	reader() =========");
        
        return new StaxEventItemReaderBuilder<Situation>()
                .name("reader")
                .addFragmentRootElements("situationRecord")
                .unmarshaller(situationMarshaller)
                .strict(false)	// catch exception when resource doesn't exist, and log nothing
                .build();        
    }

    /**
     * The ItemProcessor is called after a new line is read and it allows the developer
     * to transform the data read
     * In our example it simply return the original object
     *
     * @return
     */
    @Bean
    public ItemProcessor<Situation, Situation> processor() {
    	 logger.debug("==========	processor() =========");
        return new SituationItemProcessor();
    }
    


    /**
     * Nothing special here a simple JpaItemWriter
     * @return
     */
    @Bean
    public ItemWriter<Situation> writer() {
        JpaItemWriter<Situation> writer = new JpaItemWriter<Situation>();
        writer.setEntityManagerFactory(entityManagerFactory().getObject());
        logger.debug("==========	writer() =========");
        return writer;
    }

            
    /**
     * Déplacer le fichier traité
     */
    @Bean
	public Step moveFileStep() {
	    return stepBuilderFactory
	   		.get("moveFileStep")
	        .tasklet(fileMovingTasklet())
	        .build();
	}
    
    /**
     * Moving processed files
     */
	@Bean
	@StepScope
	public Tasklet fileMovingTasklet() {

		String sDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		org.springframework.core.io.Resource[] resources = null;
		try {
			resources = patternResolver.getResources(Constantes.PREFIXE + inputResources + extensionsFiles);

			int i = 1;
			for(Resource r: resources) {
				logger.debug("Déplacement du fichier : " + r.getURI());			
				Path source = Paths.get(r.getURI());
				Path sourceTarget = Paths.get(targetResources 
					+ Constantes.ANTISLASH 
					+ Constantes.S_DATE_NAME 
					+ i++ 
					+ Constantes.UNDERSCORE
					+ sDt 
					+ Constantes.EXTENSION);
				try {
				Files.move(source, sourceTarget, StandardCopyOption.REPLACE_EXISTING);
				} catch(Exception ex) {
				logger.debug("le fichier n'est pas déplacé : " + ex.getMessage());
				}
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage());
		}

		return new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					return RepeatStatus.FINISHED;
				}
		};
	}

    /**
     * As data source we use an external database
     *
     * @return
     */

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseDriver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setPackagesToScan("com.fhi.datex");
        lef.setDataSource(dataSource());
        lef.setJpaVendorAdapter(jpaVendorAdapter());
        lef.setJpaProperties(new Properties());
        return lef;
    }


    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.ORACLE);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(false);

        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.Oracle8iDialect");
        return jpaVendorAdapter;
    }

}
