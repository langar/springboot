package com.fhi.datex.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import com.fhi.datex.utils.LocalDateTimeAdapter;

import lombok.Data;

@Entity
@Data
@XmlRootElement(name="situationRecord")
//@XmlType(propOrder={"id", "country", "publicationTime"})
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name="D_ALERTE_EXTERNE")
public class Situation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // GenerationType.IDENTITY)
    private Long id;
    
//    @XmlPath("payloadPublication/publicationCreator/country/text()")
//    private String country;
    
//    @XmlPath("payloadPublication/publicationTime/text()")
//    private Date publicationTime;
    
//    @Id
	private long ale_cts_kn = Long.valueOf("99010001101");
    
   
   // date création 
    @XmlPath("situationRecordCreationTime/text()")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime ale_kd_app;
    
    private LocalDateTime ale_vd_maj = LocalDateTime.now();
    
    @Transient
    private int ale_vn_nbapp;
    
    @Transient
    private int ale_vn_etat;
    
    @Transient
    private String ale_description;
    
    @XmlPath("groupOfLocations/locationContainedInItinerary[@index='1']/location/pointAlongLinearElement/distanceAlongLinearElement/distanceAlong/text()")
    private Double ALE_VN_PKDEB; 
    
    
    @XmlPath("groupOfLocations/locationContainedInItinerary[@index='1']/location/pointAlongLinearElement/directionBoundAtPoint/text()")
    private String ALE_SEN_KN;
    
    @Transient
    @XmlPath("groupOfLocations/locationContainedInItinerary[@index='1']/location/pointAlongLinearElement/linearElement/roadNumber/text()")
    private String ale_axe_va_nom;
    
    @Transient
    @XmlPath("groupOfLocations/locationContainedInItinerary[@index='1']/location/pointAlongLinearElement/linearElement/roadNumber/text()")
    private String axe_fin;
    
    @Transient
    @XmlPath("groupOfLocations/locationContainedInItinerary[@index='2']/location/pointAlongLinearElement/directionBoundAtPoint/text()")
    private String direction_fin; // m11 direction kp
    
    @Transient
    @XmlPath("groupOfLocations/locationContainedInItinerary[@index='2']/location/pointAlongLinearElement/distanceAlongLinearElement/distanceAlong/text()")
    private Long pk_fin;
    
    @Transient
    @XmlPath("management/lifeCycleManagement/end/text()")
    private boolean etat;
    
    
    @Transient
    @XmlPath("@xsi:type")
    private String type;
    
    @XmlPath("@id")
    private String ale_id_datexii;
  
    @Transient
    @XmlPath("groupOfLocations/locationContainedInItinerary[@index='1']/location/supplementaryPositionalDescription/affectedCarriagewayAndLanes/carriageway/text()")
    private String carriageway;
    
    @Transient
    @XmlPath("groupOfLocations/locationContainedInItinerary[@index='2']/location/pointAlongLinearElement/linearElement/roadNumber/text()")
    private String axe_return;
  
    
}
