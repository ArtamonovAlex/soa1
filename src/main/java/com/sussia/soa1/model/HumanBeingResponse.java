package com.sussia.soa1.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;
import java.util.List;

@XmlRootElement(name = "root")
public class HumanBeingResponse {
    @XmlElement
    public HumanBeings data;
    @XmlElement
    public Meta meta;

    protected HumanBeingResponse() {}

    public HumanBeingResponse(HumanBeings data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }

    public String toXml() throws JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(HumanBeingResponse.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(this, sw);
        return sw.toString();
    }
}
