package es.us.isa.ppinot.resource;

import es.us.isa.bpms.repository.ProcessRepository;
import es.us.isa.bpms.users.UserService;
import es.us.isa.ppinot.handler.PPINotModelHandler;
import es.us.isa.ppinot.handler.PPINotModelHandlerImpl;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.PPISet;
import org.jboss.resteasy.spi.UnauthorizedException;

import javax.ws.rs.*;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: resinas
 * Date: 20/05/13
 * Time: 22:06
 */
public class PPINOTResource {

    private UserService userService;
    private ProcessRepository processRepository;
    private String id;
    private InputStream processStream;

    public PPINOTResource(InputStream processStream, String id, UserService userService, ProcessRepository processRepository) {
        this.processStream = processStream;
        this.id = id;
        this.userService = userService;
        this.processRepository = processRepository;
    }

    @Produces("application/json")
    @GET
    public Collection<PPISet> getPPIs(@PathParam("id") String id) {
        PPINotModelHandler handler = getPpiNotModelHandler(id);
        return handler.getPPISets();
//        return handler.getPpiModelMap().values();
    }

    @Consumes("application/json")
    @POST
    public String storePPIs(@PathParam("id") String id, List<PPI> ppis) {
        if (! userService.isLogged())
            throw new UnauthorizedException("User not logged");

        PPINotModelHandler ppinotModelHandler = getPpiNotModelHandler(id);

        Map<String, PPI> ppiModelMap = new HashMap<String, PPI>();
        for (PPI p: ppis) {
            ppiModelMap.put(p.getId(), p);
        }

//        ppinotModelHandler.setPpiModelMap(ppiModelMap);

        try {
            String procId = ppinotModelHandler.getProcId();
            ppinotModelHandler.save(processRepository.getProcessWriter(id+"-copy-"), procId);

            return "Ok";
        } catch (Exception e) {
            throw new RuntimeException("unable to create file", e);
        }


    }

    private PPINotModelHandler getPpiNotModelHandler(String id) {
        PPINotModelHandler ppinotModelHandler;
        try {
            ppinotModelHandler = new PPINotModelHandlerImpl();
            ppinotModelHandler.load(processStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Problem loading PPIs of process " + id, e);
        }
        return ppinotModelHandler;
    }
}
