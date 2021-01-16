package com.sussia.soa1.model;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Set;

@Entity
@XmlRootElement
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @XmlElement
    private boolean cool;

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL)
    private HumanBeing humanBeing;
}
