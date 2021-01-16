package com.sussia.soa1.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Meta {
    @XmlElement
    public int page;
    @XmlElement
    public int count;

    protected Meta() {}

    public Meta(int page, int count) {
        this.page = page;
        this.count = count;
    }

}
