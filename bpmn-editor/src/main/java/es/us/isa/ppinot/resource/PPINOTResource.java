package es.us.isa.ppinot.resource;

import es.us.isa.bpms.repository.ProcessRepository;
import es.us.isa.bpms.users.UserService;
import es.us.isa.ppinot.handler.PpiNotModelHandler;
import es.us.isa.ppinot.handler.PpiNotModelHandlerInterface;
import es.us.isa.ppinot.model.PPI;
import org.jboss.resteasy.spi.UnauthorizedException;

import javax.ws.rs.*;
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

    public PPINOTResource(String id, UserService userService, ProcessRepository processRepository) {
        this.id = id;
        this.userService = userService;
        this.processRepository = processRepository;
    }

    @Produces("application/json")
    @GET
    public Collection<PPI> getPPIs(@PathParam("id") String id) {
        try {
            PpiNotModelHandlerInterface handler = new PpiNotModelHandler();
            handler.load(processRepository.getProcessReader(id));
            return handler.getPpiModelMap().values();
        } catch (Exception e) {
            throw new RuntimeException("Problem loading PPIs of process " + id, e);
        }

    }

    @Consumes("application/json")
    @POST
    public String storePPIs(@PathParam("id") String id, List<PPI> ppis) {
        if (! userService.isLogged())
            throw new UnauthorizedException("User not logged");

        PpiNotModelHandlerInterface ppinotModelHandler;
        try {
            ppinotModelHandler = new PpiNotModelHandler();
            ppinotModelHandler.load(processRepository.getProcessReader(id));
        } catch (Exception e) {
            throw new RuntimeException("Problem loading PPIs of process " + id, e);
        }

        Map<String, PPI> ppiModelMap = new HashMap<String, PPI>();
        for (PPI p: ppis) {
            ppiModelMap.put(p.getId(), p);
        }

        ppinotModelHandler.setPpiModelMap(ppiModelMap);

        try {
            String procId = ppinotModelHandler.getProcId();
            ppinotModelHandler.save(processRepository.getProcessWriter(id+"-copy-"), procId);

            return "Ok";
        } catch (Exception e) {
            throw new RuntimeException("unable to create file", e);
        }


    }
}
