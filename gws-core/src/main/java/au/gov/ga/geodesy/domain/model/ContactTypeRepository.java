package au.gov.ga.geodesy.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContactTypeRepository extends JpaRepository<ContactType, Integer> {

    @Override
    List<ContactType> findAll();

    ContactType findByCode(String code);

    @Query("select c from ContactType c where c.code = '" + ContactType.SITE_CONTACT + "'")
    ContactType siteContact();

    @Query("select c from ContactType c where c.code = '" + ContactType.SITE_METADATA_CUSTODIAN + "'")
    ContactType siteMetadataCustodian();
}

