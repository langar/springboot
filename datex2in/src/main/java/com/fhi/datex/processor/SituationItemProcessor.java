package com.fhi.datex.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import com.fhi.datex.model.Situation;

public class SituationItemProcessor implements ItemProcessor<Situation, Situation> {
	
private static final Logger logger = LoggerFactory.getLogger(SituationItemProcessor.class);
	
	
	@Value("${cts_kn}")
	private String cts_kn;
	
	public SituationItemProcessor() {
		
	}

	@Override
    public Situation process(final Situation situation) throws Exception {
		
		logger.debug("Start process..");
		
        situation.setAle_cts_kn(Long.valueOf(cts_kn));
        
		situation.setAle_description(situation.getType() + " " + situation.getAle_id_datexii());		
		situation.setAle_vn_etat(situation.isEtat() ? 0 : 1);		
		situation.setAle_vn_nbapp(6);
		
		logger.debug(" => ID : " + situation.getAle_cts_kn());
		logger.debug(" => MAJ : " + situation.getAle_vd_maj());
		logger.debug(" => Date création : " + situation.getAle_kd_app());
		logger.debug(" => Date MAJ : " + situation.getAle_vd_maj());
		logger.debug(" => NBAPP number : " + situation.getAle_vn_nbapp());
		logger.debug(" => ETAT : " + situation.getAle_vn_etat());		
		logger.debug(" => Axe : " + situation.getAle_axe_va_nom());		
		logger.debug(" => SEN : " + situation.getALE_SEN_KN());		
		logger.debug(" => PK début : " + situation.getALE_VN_PKDEB());		
		logger.debug(" => Description : " + situation.getAle_description());		
		
		logger.debug("End process..");
        if(situation.getAle_kd_app() == null || situation.getAle_cts_kn() == 0) {
        	return null;
        }
        
        return situation;
    }
}