package com.subocol.manage.purchase.common.utils;

import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.models.IntegrationAWS;
import com.subocol.manage.purchase.infrastructure.persistence.entities.IntegrationAWSModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.PropertiesModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.IntegrationAWSRepository;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.PropertiesRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class SingletonProperties {

    private HashMap<String, Long> propertiesId;

    private HashMap<String, Long> integrationAWSId;

    private List<PropertiesModel> propertiesList;

    private List<IntegrationAWSModel> awsList;

    private final PropertiesRepository propertiesRepository;

    private final IntegrationAWSRepository integrationAWSRepository;

    @PostConstruct
    public void initialize() {
        initMaps();
    }


    private void initMaps() {
        this.propertiesId = new HashMap<>();
        this.integrationAWSId = new HashMap<>();

        this.propertiesId.put(PropertiesConstants.LOCATION_CREATION_EP, 1L);
        this.propertiesId.put(PropertiesConstants.LOCATION_UPDATING_EP, 2L);
        this.propertiesId.put(PropertiesConstants.LOCATION_DELETING_EP, 3L);
        this.propertiesId.put(PropertiesConstants.LOCATION_FIND_EP, 4L);
        this.propertiesId.put(PropertiesConstants.USER_SERVICE_ROOT, 5L);
        this.propertiesId.put(PropertiesConstants.USER_CREATION_EP, 6L);
        this.propertiesId.put(PropertiesConstants.USER_PROVIDER_EP, 7L);
        this.propertiesId.put(PropertiesConstants.USER_EMAIL_EP, 8L);
        this.propertiesId.put(PropertiesConstants.USER_SUBSIDIARY_EP, 9L);
        this.propertiesId.put(PropertiesConstants.TIME_DELIVERY_CITY, 10L);
        this.propertiesId.put(PropertiesConstants.TIME_DELIVERY_REGION, 11L);
        this.propertiesId.put(PropertiesConstants.TIME_DELIVERY_REGION_LOCATION, 12L);
        this.propertiesId.put(PropertiesConstants.NOTIFICATION_BASE_PATH, 13L);
        this.propertiesId.put(PropertiesConstants.LOCATION_BASE_PATH, 14L);
        this.propertiesId.put(PropertiesConstants.ADI_BASE_PATH, 15L);
        this.propertiesId.put(PropertiesConstants.ADI_BASE_PATH_QUOTE, 16L);
        this.propertiesId.put(PropertiesConstants.ADI_BASE_PATH_SUGGESTED_REFERENCE, 17L);
        this.propertiesId.put(PropertiesConstants.VALUATION_QUOTE_PIECES, 18L);
        this.propertiesId.put(PropertiesConstants.ORDERS_BILLING_MANAGEMENT, 19L);
        this.propertiesId.put(PropertiesConstants.ADMIN_BILLING_CALCULATION, 20L);
        this.propertiesId.put(PropertiesConstants.ADMIN_GET_TALLER_BY_USER, 24L);
        this.propertiesId.put(PropertiesConstants.TIMEOUT_SERVICE_IA, 25L);
        this.propertiesId.put(PropertiesConstants.ADMIN_RESERVE_SURA, 26L);
        this.propertiesId.put(PropertiesConstants.MANAGE_NOTICE_MM_PATH, 27L);
        this.propertiesId.put(PropertiesConstants.FOLLOWUP_SEND_INFORMATION, 28L);
        this.propertiesId.put(PropertiesConstants.AUTH_VALUATION_SERVICES, 30L);
        this.integrationAWSId.put(PropertiesConstants.AWS_BUCKET_NAME, 1L);
        this.integrationAWSId.put(PropertiesConstants.AWS_ACCESS_KEY, 1L);
        this.integrationAWSId.put(PropertiesConstants.AWS_SECRET_ACCESS_KEY, 1L);
        this.integrationAWSId.put(PropertiesConstants.AWS_CREDENTIALS_CHILE, 3L);

    }

    public String getPropertyByKey(String key) {
        return propertiesList.stream()
                .filter(property -> Objects.equals(property.getId(), getPropertyId(key)))
                .findFirst()
                .orElse(new PropertiesModel())
                .getProperty();
    }

    public IntegrationAWS getIntegrationAWSByKey(String key) {
        return MapperUtil.convert(awsList.stream()
                .filter(property -> Objects.equals(property.getId(), getIntegrationAWSId(key)))
                .findFirst()
                .orElse(new IntegrationAWSModel()), IntegrationAWS.class);
    }

    public Long getPropertyId(String key) {
        return this.propertiesId.get(key);
    }

    public Long getIntegrationAWSId(String key) {
        return this.integrationAWSId.get(key);
    }

    public String getCurrentPropertyByKey(String key) throws ExceptionUtil {

        Long propertyId = getPropertyId(key);
        if (propertyId != null) {
            Optional<PropertiesModel> propertyOptional = propertiesRepository.findById(propertyId);

            return propertyOptional.orElseThrow(() ->
                    new ExceptionUtil(HttpStatus.NOT_FOUND.value(),
                            ErrorMessageHandler.concatenateStringAndObject(ErrorMessageHandler.PROPERTY_NOT_FOUND, propertyId))
            ).getProperty();

        } else {
            log.info(ErrorMessageHandler.PROPERTY_NOT_FOUND_IN_HASHMAP + key);
            throw new ExceptionUtil(HttpStatus.NOT_FOUND.value(),
                    ErrorMessageHandler.concatenateStringAndObject(ErrorMessageHandler.PROPERTY_NOT_FOUND_IN_HASHMAP, key));
        }
    }

    public IntegrationAWS getCurrentAwsPropertyByKey(String key) throws ExceptionUtil {

        Long propertyId = getIntegrationAWSId(key);
        if (propertyId != null) {
            Optional<IntegrationAWSModel> result = integrationAWSRepository.findById(propertyId);

            IntegrationAWS integrationAWS = MapperUtil.convert(result.orElseThrow(() ->
                    new ExceptionUtil(HttpStatus.NOT_FOUND.value(),
                            ErrorMessageHandler.concatenateStringAndObject(ErrorMessageHandler.AWS_PROPERTY_NOT_FOUND, propertyId))
            ), IntegrationAWS.class);

            String accessKey = integrationAWS.getAccessKey();
            String secretAccessKey = integrationAWS.getSecretAccessKey();
            return integrationAWS.setAccessKey(new String(Base64.getDecoder().decode(accessKey)))
                    .setSecretAccessKey(new String(Base64.getDecoder().decode(secretAccessKey)));

        } else {
            log.info(ErrorMessageHandler.PROPERTY_NOT_FOUND_IN_HASHMAP + key);
            throw new ExceptionUtil(HttpStatus.NOT_FOUND.value(),
                    ErrorMessageHandler.concatenateStringAndObject(ErrorMessageHandler.PROPERTY_NOT_FOUND_IN_HASHMAP, key));
        }
    }

}



