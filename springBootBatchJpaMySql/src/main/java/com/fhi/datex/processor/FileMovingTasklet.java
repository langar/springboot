package com.fhi.datex.processor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.fhi.datex.utils.Constantes;

import lombok.Data;

/**
 * Déplacer le fichier
 */
@Data
public class FileMovingTasklet implements Tasklet, InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(FileMovingTasklet.class);
	
	private org.springframework.core.io.Resource[] resources;
    private String targetResources;
 
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    	
//    	logger.debug("Déplacement des fichiers traités.");
////		System.out.println(dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//		String sDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        
//		int i = 1;
//		for(Resource r: resources) {
//			logger.debug("Déplacement du fichier : " + r.getURI());			
//			Path source = Paths.get(r.getURI());
//	    	Path sourceTarget = Paths.get(getTargetResources() 
//	    			+ Constantes.ANTISLASH 
//	    			+ Constantes.S_DATE_NAME 
//	    			+ i++ 
//	    			+ Constantes.UNDERSCORE
//	    			+ sDt 
//	    			+ Constantes.EXTENSION);
//	    	try {
//	    		Files.move(source, sourceTarget, StandardCopyOption.REPLACE_EXISTING);
//	    	} catch(Exception ex) {
//	    		logger.debug("le fichier n'est pas déplacé : " + ex.getMessage());
//	    	}
//		}
    	
        return RepeatStatus.FINISHED;
    }
 
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resources, "directory must be set");
    }
}
