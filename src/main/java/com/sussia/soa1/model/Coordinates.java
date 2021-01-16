package com.sussia.soa1.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Set;

@Entity
@XmlRootElement
public class Coordinates implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @XmlElement
    private long x;

    //Максимальное значение поля: 421
    @XmlElement
    private float y;

    @OneToOne(mappedBy = "coordinates", cascade = CascadeType.ALL)
    private HumanBeing humanBeing;
}
