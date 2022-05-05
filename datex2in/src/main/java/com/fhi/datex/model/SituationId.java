package com.fhi.datex.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SituationId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4517824365881661063L;

	private long ale_cts_kn;
	
	private LocalDateTime ale_kd_app;
	
	public SituationId(long ale_cts_kn, LocalDateTime ale_kd_app) {
		this.ale_cts_kn = ale_cts_kn;
		this.ale_kd_app = ale_kd_app;
	}
	
	public SituationId () {
		
	}

}
