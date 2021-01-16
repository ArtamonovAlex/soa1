package com.sussia.soa1.servlets;

import com.sussia.soa1.model.*;
import com.sussia.soa1.services.HumanBeingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.xml.sax.SAXParseException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/human-beings")
public class HumanServlet extends HttpServlet {

    @Autowired
    private HumanBeingService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int page = 1;
            int count = 20;
            Sort sort = Sort.by("id").ascending();
            Specification<HumanBeing> filter = idGreaterThenZero();
            Map<String, String[]> params = req.getParameterMap();
            for (Map.Entry<String, String[]> entry : params.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if(values.length != 1) {
                    resp.setStatus(400);
                    return;
                }
                String value = values[0];
                switch (key) {
                    case "filter":
                        filter = prepareFilter(value);
                        if (filter == null) {
                            resp.setStatus(400);
                            return;
                        }
                        break;
                    case "sort":
                        sort = prepareSort(value);
                        if (sort == null) {
                            resp.setStatus(400);
                            return;
                        }
                        break;
                    case "page":
                        try {
                            page = Integer.parseInt(value);
                            if (page < 1) throw new NumberFormatException();
                        }
                        catch (NumberFormatException e)
                        {
                            resp.setStatus(400);
                            return;
                        }
                        break;
                    case "count":
                        try {
                            count = Integer.parseInt(value);
                            if (count < 1) throw new NumberFormatException();
                        }
                        catch (NumberFormatException e)
                        {
                            resp.setStatus(400);
                            return;
                        }
                        break;
                    default:
                        resp.setStatus(400);
                        return;
                }
            }


            String beings = service.getAll(new Meta(page, count), sort, filter);
            resp.setContentType("application/xml");
            resp.getWriter().println(beings);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String xml =  new BufferedReader(new InputStreamReader(req.getInputStream())).lines().collect(
                Collectors.joining("\n"));
        if (xml.trim().length() == 0) {
            // return error that jsonBody is empty
            resp.setStatus(400);
            return;
        }
        try {
            service.save(xml);
            resp.setStatus(201);
        }
        catch (UnmarshalException e) {
            Throwable linked = e.getLinkedException();
            if (linked instanceof SAXParseException) {
                SAXParseException parseExp = (SAXParseException) linked;
                System.out.println(parseExp.getMessage().replaceFirst(".*: ", ""));
                resp.setStatus(400);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Sort prepareSort(String sortQuery) {
        List<Sort> sorts = new ArrayList<Sort>();
        Sort globalSort = null;
        String[] pairs = sortQuery.split(",");
        for(String pair : pairs){
            Sort sort = null;
            String[] kv = pair.split(":");
            if (kv.length != 2) {
                return null;
            }
            switch (kv[0]) {
                case "id":
                    sort = Sort.by("id");
                    break;
                case "name":
                    sort = Sort.by("name");
                    break;
                case "creationDate":
                    sort = Sort.by("creationDate");
                    break;
                case "realHero":
                    sort = Sort.by("realHero");
                    break;
                case "hasToothpick":
                    sort = Sort.by("hasToothpick");
                    break;
                case "impactSpeed":
                    sort = Sort.by("impactSpeed");
                    break;
                case "minutesOfWaiting":
                    sort = Sort.by("minutesOfWaiting");
                    break;
                case "weaponType":
                    sort = Sort.by("weaponType");
                    break;
                case "mood":
                    sort = Sort.by("mood");
                    break;
                case "coordinates.x":
                    sort = Sort.by("coordinates.x");
                    break;
                case "coordinates.y":
                    sort = Sort.by("coordinates.y");
                    break;
                case "car.cool":
                    sort = Sort.by("car.cool");
                    break;
                default:
                    return null;
            }
            switch (kv[1]) {
                case "asc":
                    sort = sort.ascending();
                    break;
                case "desc":
                    sort = sort.descending();
                    break;
                default:
                    return null;
            }
            sorts.add(sort);
        }

        for(Sort s: sorts) {
            if (globalSort == null) {
                globalSort = s;
            } else {
                globalSort = globalSort.and(s);
            }
        }
        return globalSort == null ? Sort.by("id").ascending(): globalSort;
    }

    private Specification<HumanBeing> prepareFilter(String filterQuery) {
        System.out.println("adfsgdhfjgkhlgf");
        List<Specification<HumanBeing>> filters = new ArrayList<Specification<HumanBeing>>();
        Specification<HumanBeing> globalFilter = null;
        String[] pairs = filterQuery.split(",");
        for(String pair : pairs){
            Specification<HumanBeing> filter = null;
            String[] kv = pair.split(":");
            if (kv.length != 2) {
                return null;
            }
            switch (kv[0]) {
                case "id":
                    Long id = tryParseLong(kv[1]);
                    if (id != null) {
                        filter = humanAttributeEquals("id", id);
                        break;
                    } else {return null;}
                case "name":
                    filter = humanAttributeEquals("name", kv[1]);
                    break;
                case "creationDate":
                    filter = humanAttributeEquals("creationDate", kv[0]);
                    break;
                case "realHero":
                    Boolean realHero = tryParseBool(kv[1]);
                    if (realHero != null) {
                        filter = humanAttributeEquals("realHero", realHero);
                        break;
                    } else {return null;}
                case "hasToothpick":
                    Boolean hasToothpick = tryParseBool(kv[1]);
                    if (hasToothpick != null) {
                        filter = humanAttributeEquals("hasToothpick", hasToothpick);
                        break;
                    } else {return null;}
                case "impactSpeed":
                    Float impactSpeed = tryParseFloat(kv[1]);
                    if (impactSpeed != null) {
                        filter = humanAttributeEquals("impactSpeed", impactSpeed);
                        break;
                    } else {return null;}
                case "minutesOfWaiting":
                    Long minutesOfWaiting = tryParseLong(kv[1]);
                    if (minutesOfWaiting != null) {
                        filter = humanAttributeEquals("minutesOfWaiting", minutesOfWaiting);
                        break;
                    } else {return null;}
                case "weaponType":
                    try {
                        filter = weaponTypeEquals(WeaponType.valueOf(kv[1]));
                        break;
                    } catch (Exception e) {
                        return null;
                    }
                case "mood":
                    try {
                        filter = moodEquals(Mood.valueOf(kv[1]));
                        break;
                    } catch (Exception e) {
                        return null;
                    }
                case "coordinates.x":
                    Long coordinatesX = tryParseLong(kv[1]);
                    if (coordinatesX != null) {
                        filter = coordinatesAttributeEquals("x", coordinatesX);
                        break;
                    } else {return null;}
                case "coordinates.y":
                    Float coordinatesY = tryParseFloat(kv[1]);
                    if (coordinatesY != null) {
                        filter = coordinatesAttributeEquals("y", coordinatesY);
                        break;
                    } else {return null;}
                case "car.cool":
                    Boolean cool = tryParseBool(kv[1]);
                    if (cool != null) {
                        filter = carAttributeEquals("cool", cool);
                        break;
                    } else {return null;}

                default:
                    return null;
            }
            filters.add(filter);
        }

        for(Specification<HumanBeing> s: filters) {
            if (globalFilter == null) {
                globalFilter = s;
            } else {
                globalFilter = globalFilter.and(s);
            }
        }
        return globalFilter == null ? idGreaterThenZero() : globalFilter;
    }

    private Specification<HumanBeing> humanAttributeEquals(String attribute, String value) {
        return (root, query, cb) -> {
            if(value == null) {
                return null;
            }
            return cb.equal(root.get(attribute), value);
        };
    }

    private Specification<HumanBeing> humanAttributeEquals(String attribute, Long value) {
        return (root, query, cb) -> {
            if(value == null) {
                return null;
            }
            return cb.equal(root.get(attribute), value);
        };
    }

    private Specification<HumanBeing> idGreaterThenZero() {
        return (root, query, cb) -> cb.greaterThan(root.get("id"), 0);
    }

    private Specification<HumanBeing> humanAttributeEquals(String attribute, Float value) {
        return (root, query, cb) -> {
            if(value == null) {
                return null;
            }
            return cb.equal(root.get(attribute), value);
        };
    }

    private Specification<HumanBeing> humanAttributeEquals(String attribute, Boolean value) {
        return (root, query, cb) -> {
            if(value == null) {
                return null;
            }
            return cb.equal(root.get(attribute), value);
        };
    }

    private Specification<HumanBeing> coordinatesAttributeEquals(String attribute, Long value) {
        return (root, query, cb) -> {
            if(value == null) {
                return null;
            }
            Join<HumanBeing, Coordinates> coordinates = root.join("coordinates", JoinType.INNER);
            return cb.equal(coordinates.get(attribute), value);
        };
    }

    private Specification<HumanBeing> coordinatesAttributeEquals(String attribute, Float value) {
        return (root, query, cb) -> {
            if(value == null) {
                return null;
            }
            Join<HumanBeing, Coordinates> coordinates = root.join("coordinates", JoinType.INNER);
            return cb.equal(coordinates.get(attribute), value);
        };
    }

    private Specification<HumanBeing> carAttributeEquals(String attribute, Boolean value) {
        return (root, query, cb) -> {
            if(value == null) {
                return null;
            }
            Join<HumanBeing, Car> cars = root.join("car", JoinType.INNER);
            return cb.equal(cars.get(attribute), value);
        };
    }

    private Specification<HumanBeing> moodEquals(Mood value) {
        return (root, query, cb) -> {
            if(value == null) {
                return null;
            }
            return cb.equal(root.get("mood"), value);
        };
    }

    private Specification<HumanBeing> weaponTypeEquals(WeaponType value) {
        return (root, query, cb) -> {
            if(value == null) {
                return null;
            }
            return cb.equal(root.get("weaponType"), value);
        };
    }

    private Specification<HumanBeing> humanAttributeEqualsIgnoreCase(String attribute, String value) {
        return (root, query, cb) -> {
            if(value == null) {
                return null;
            }
            Join<HumanBeing, Car> cars = root.join("car", JoinType.INNER);
            return cb.equal(cars.get(attribute), value);
        };
    }

    private Long tryParseLong(String strLong) {
        try {
            long num = Long.parseLong(strLong);
            if (num < 1) throw new Exception("");
            return num;
        } catch (Exception e) {
            return null;
        }
    }

    private Float tryParseFloat(String strFloat) {
        try {
            float num = Float.parseFloat(strFloat);
            if (num < 0) throw new Exception("");
            return num;
        } catch (Exception e) {
            return null;
        }
    }

    private Boolean tryParseBool(String stringBool) {
        switch (stringBool) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                return null;
        }
    }
}
