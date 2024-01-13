package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.models.Location;
import com.subocol.manage.purchase.domain.ports.externalservices.LocationExternalServicesPort;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.LocationExternalServicesAdapter;
import com.subocol.manage.purchase.infrastructure.externalservices.dtos.CityDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.dtos.CountryDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.dtos.LocationDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.dtos.RegionDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LocationServiceTest {
    @Spy
    private RestTemplate restTemplate;

    @InjectMocks
    private LocationExternalServicesAdapter locationExternalServicesAdapter;

    @Mock
    private SingletonProperties propertiesBean;

    LocationDTO locationDTO = new LocationDTO();


    @BeforeEach
    void setup() {
//        locationExternalServicesPort = new LocationExternalServicesAdapter(restTemplate);

        locationDTO = LocationDTO.builder().id(24898L).address("123 Main St").build();

        CityDTO cityDTO = CityDTO.builder().id(1L).name("CityName").state("State").isoCode("ISO").build();

        RegionDTO regionDTO = RegionDTO.builder().id(1L).name("RegionName").build();

        CountryDTO countryDTO = CountryDTO.builder().id(1L).name("CountryName").build();

        regionDTO.setCountry(countryDTO);
        cityDTO.setRegion(regionDTO);
        locationDTO.setCity(cityDTO);

    }

    @Test
    void testFindLocationUseRealExternalService() {

        String address = "Carretera Interamericana Coquito Chiriquí";
        Long id=1L;
        Long countryId=2L;

        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.LOCATION_FIND_EP))
                .thenReturn("https://liferaydev.subocol.com/o/LocationCompraDigitalPortlet/api/location/found/%s");

        Location location = locationExternalServicesAdapter.findLocation(id);
        log.info(location.toString());
        assertEquals(id, location.getId());
        assertEquals(address, location.getAddress());
        assertEquals(countryId, location.getCountryId());

    }
    @Test
    void testFindLocation() {

        Long id=1L;
        String url = "https://liferaydev.subocol.com/o/LocationCompraDigitalPortlet/api/location/found/%s";
        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.LOCATION_FIND_EP))
                .thenReturn(url);


        Mockito.when(restTemplate.getForObject(String.format(url, id), LocationDTO.class))
                .thenReturn(locationDTO);

        Location location = locationExternalServicesAdapter.findLocation(1L);

        assertEquals(locationDTO.getId(), location.getId());
        assertEquals(locationDTO.getAddress(), location.getAddress());
        assertEquals(locationDTO.getCity().getRegion().getCountry().getId(), location.getCountryId());

    }
}

