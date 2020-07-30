package br.com.netris.api;

import br.com.netris.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/{module}/{entity}", method = RequestMethod.GET)
    public List findResources(
            @PathVariable String module,
            @PathVariable String entity,
            @RequestParam HashMap<String, String> params) throws ClassNotFoundException {
        return resourceService.findAll(module, entity, params);
    }
}