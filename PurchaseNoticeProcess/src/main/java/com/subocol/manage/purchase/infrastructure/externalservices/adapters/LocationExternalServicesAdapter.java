package com.subocol.manage.purchase.infrastructure.externalservices.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.models.Location;
import com.subocol.manage.purchase.domain.ports.externalservices.LocationExternalServicesPort;
import com.subocol.manage.purchase.infrastructure.externalservices.dtos.LocationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Adapter
@Slf4j
public class LocationExternalServicesAdapter implements LocationExternalServicesPort {

    private final RestTemplate restTemplate;

    private final SingletonProperties propertiesBean;

    public LocationExternalServicesAdapter(RestTemplate restTemplate, SingletonProperties propertiesBean) {
        this.restTemplate = restTemplate;
        this.propertiesBean = propertiesBean;
    }

    @Override
    public Location findLocation(Long id) {
       String url=String.format(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.LOCATION_FIND_EP), id);
       log.info(url);
       LocationDTO locationDTO = restTemplate.getForObject(url, LocationDTO.class);

        Location location = new Location();

        if (locationDTO != null) {
            location.setId(locationDTO.getId())
                    .setAddress(locationDTO.getAddress())
                    .setCountryId(locationDTO.getCity().getRegion().getCountry().getId())
                    .setCityName(locationDTO.getCity().getName());
        }

        return location;
    }
}
