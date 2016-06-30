package au.gov.ga.geodesy.port.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import au.gov.ga.geodesy.domain.model.equipment.GnssReceiver;
import au.gov.ga.geodesy.domain.model.equipment.GnssReceiverRepository;
import au.gov.ga.geodesy.domain.model.equipment.QGnssReceiver;

/**
 * GnssReceiver HTTP endpoint.
 */
@RepositoryRestController
public class GnssReceiverEndpoint {

    @Autowired
    private GnssReceiverRepository receivers;

    @Autowired
    private PagedResourcesAssembler<GnssReceiver> assembler;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

    /**
     * Find GnssReceivers by example.
     */
    @RequestMapping(
        value = "/gnssReceivers",
        method = RequestMethod.GET,
        produces = "application/hal+json")

    public ResponseEntity<PagedResources<Resource<GnssReceiver>>> findByExample(
            GnssReceiver receiver,
            PersistentEntityResourceAssembler entityAssembler,
            Pageable pageRequest) {

        BooleanBuilder builder = new BooleanBuilder();
        QGnssReceiver qReceiver = QGnssReceiver.gnssReceiver;

        // TODO: find out why we can't just say qReceiver.eq(receiver),
        // or check all attributes individually

        if (receiver.getType() != null) {
            builder.and(qReceiver.type.eq(receiver.getType()));
        }
        Predicate receiverPredicate = builder.getValue();
        Page<GnssReceiver> page = receivers.findAll(receiverPredicate, pageRequest);

        @SuppressWarnings({"unchecked", "rawtypes"})
        PagedResources<Resource<GnssReceiver>> paged = assembler.toResource(page,
            (ResourceAssembler) entityAssembler);

        return new ResponseEntity<>(paged, new HttpHeaders(), HttpStatus.OK);
    }
}
