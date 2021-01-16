package com.sussia.soa1.model;

import io.github.threetenjaxb.core.ZonedDateTimeXmlAdapter;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;
import javax.persistence.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;

@Entity
@XmlRootElement
public class HumanBeing implements Serializable {

    //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute
    public long id;

    //Поле не может быть null, Строка не может быть пустой
    @Column(nullable = false)
    @XmlElement(required=true)
    private String name;

    //Поле не может быть null
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="coordinates_id", referencedColumnName = "id", nullable = false)
    @XmlElement(required=true)
    private Coordinates coordinates;

    //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @Column(nullable = false)
    @XmlElement
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    public java.time.ZonedDateTime creationDate;

    @Column(nullable = false)
    @XmlElement(required=true)
    private boolean realHero;

    @Column(nullable = false)
    @XmlElement(required=true)
    private boolean hasToothpick;

    //Максимальное значение поля: 781
    @Column(nullable = false)
    @XmlElement(required=true)
    private float impactSpeed;

    @Column(nullable = false)
    @XmlElement(required=true)
    private long minutesOfWaiting;

    //Поле может быть null
    @XmlElement
    @Enumerated(EnumType.STRING)
    private WeaponType weaponType;

    //Поле может быть null
    @XmlElement
    @Enumerated(EnumType.STRING)
    private Mood mood;

    //Поле может быть null
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="car_id", referencedColumnName = "id")
    @XmlElement
    private Car car;

    public HumanBeing() {}

    public static HumanBeing fromXML(String xml) throws JAXBException, SAXException, IOException {
        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new ClassPathResource(
                "schemas/schema1.xsd").getFile());

        JAXBContext jaxbContext = JAXBContext.newInstance(HumanBeing.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
        return (HumanBeing) unmarshaller.unmarshal(new StringReader(xml));
    }

    public String toXml() throws JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(HumanBeing.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(this, sw);
        return sw.toString();
    }
}

