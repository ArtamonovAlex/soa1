package com.sussia.soa1.services;

import com.sussia.soa1.model.HumanBeing;
import com.sussia.soa1.model.HumanBeingResponse;
import com.sussia.soa1.model.HumanBeings;
import com.sussia.soa1.model.Meta;
import com.sussia.soa1.repositories.HumanBeingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HumanBeingService {

    @Autowired
    HumanBeingRepository humanBeingRepository;

    public String getAll(Meta meta, Sort sort, Specification<HumanBeing> filter) throws JAXBException {
        Page<HumanBeing> pagedResult = humanBeingRepository.findAll(filter, PageRequest.of(meta.page - 1, meta.count, sort));
        List<HumanBeing> list;
        if(pagedResult.hasContent()) {
            list = pagedResult.getContent();
        } else {
            list = new ArrayList<HumanBeing>();
        }

        HumanBeingResponse resp = new HumanBeingResponse(new HumanBeings(list), meta);
        return resp.toXml();
    }

    public String getAllByName(String name) throws JAXBException {
        List<HumanBeing> beingsList = (List<HumanBeing>) humanBeingRepository.findAllByNameStartsWith(name);
        HumanBeingResponse resp = new HumanBeingResponse(new HumanBeings(beingsList), new Meta(0,0));
        return resp.toXml();
    }

    public String getById(long id) throws JAXBException {
        Optional<HumanBeing> being = humanBeingRepository.findById(id);
        if (being.isPresent()) {
            return being.get().toXml();
        }
        return null;
    }

    public void delete(long id) {
        humanBeingRepository.deleteById(id);
    }

    public HumanBeing save(String xml) throws JAXBException, SAXException, IOException {
        HumanBeing humanBeing = HumanBeing.fromXML(xml);
        humanBeing.creationDate = ZonedDateTime.now();
        return humanBeingRepository.save(humanBeing);
    }

    public HumanBeing save(String xml, long id) throws JAXBException, SAXException, IOException {
        HumanBeing humanBeing = HumanBeing.fromXML(xml);
        humanBeing.id = id;
        Optional<HumanBeing> existed = humanBeingRepository.findById(id);
        existed.ifPresent(being -> humanBeing.creationDate = being.creationDate);
        return humanBeingRepository.save(humanBeing);
    }

}
