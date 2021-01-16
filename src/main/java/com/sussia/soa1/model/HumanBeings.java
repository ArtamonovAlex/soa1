package com.sussia.soa1.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

@XmlRootElement
public class HumanBeings implements Serializable {

    @XmlElement(name = "humanBeing")
    private List<HumanBeing> humanBeings;

    public HumanBeings(List<HumanBeing> beings) {
        humanBeings = beings;
    }

    protected HumanBeings() {}

    public String toXml() throws JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(HumanBeings.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(this, sw);
        return sw.toString();
    }
}
