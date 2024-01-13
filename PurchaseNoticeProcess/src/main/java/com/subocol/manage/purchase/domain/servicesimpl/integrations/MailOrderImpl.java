package com.subocol.manage.purchase.domain.servicesimpl.integrations;

import com.subocol.manage.purchase.domain.constant.Common;
import com.subocol.manage.purchase.domain.models.Subsidiary;
import com.subocol.manage.purchase.domain.ports.externalservices.MailOrderPort;
import com.subocol.manage.purchase.domain.ports.persistence.EnvironmentsRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.SubsidiaryRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.MailOrderCreateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 29/06/2023
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailOrderImpl {

    private final MailOrderPort mailOrderPort;

    private final EnvironmentsRepositoryPort environmentsRepositoryPort;

    private final SubsidiaryRepositoryPort subsidiaryRepositoryPort;

    public void addMailOrderNotification(String externalEvent, Long orderId, Integer purchaseTypeId, List<MailOrderCreateDTO> mailOrderCreateDTOS,
                                         String plate, String insurerName) {
        try {

            if (mailOrderCreateDTOS == null)
                mailOrderCreateDTOS = new ArrayList<>();

            Optional<Subsidiary> optionalSubsidiary = subsidiaryRepositoryPort.findByOrderId(orderId);
            if (optionalSubsidiary.isPresent()) {
                boolean manual = purchaseTypeId.equals(Common.PURCHASE_TYPE_MANUAL);
                mailOrderCreateDTOS.add(
                        createMailOrderDTO(externalEvent, optionalSubsidiary.get(), orderId, manual, plate, insurerName));
            }

        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }

    }

    public MailOrderCreateDTO createMailOrderDTO(String externalEvent, Subsidiary subsidiary, Long orderId, Boolean manual,
                                                 String plate, String insurerName) {

        Objects.requireNonNull(externalEvent, "externalEvent must not be null");
        Objects.requireNonNull(subsidiary, "subsidiary must not be null");
        Objects.requireNonNull(subsidiary.getName(), "subsidiaryName must not be null");
        Objects.requireNonNull(subsidiary.getEmail(), "email must not be null");
        Objects.requireNonNull(subsidiary.getId(), "subsidiaryId must not be null");
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(manual, "manual must not be null");
        Objects.requireNonNull(plate, "plate must not be null");
        Objects.requireNonNull(insurerName, "insurerName must not be null");

        return new MailOrderCreateDTO()
                .setAviso(externalEvent)
                .setSubsidiaryName(subsidiary.getName())
                .setEmail(subsidiary.getEmail())
                .setSubsidiaryId(subsidiary.getId())
                .setOrderId(orderId)
                .setManual(manual)
                .setPlate(plate)
                .setInsurerName(insurerName)
                .setLink(environmentsRepositoryPort.findEnvironmentsById(Common.ID_URL_HOME_PAGE_ORBIKA));

    }

    public void sendMailOrderNotification(List<MailOrderCreateDTO> mailOrderCreateDTOS) {
        try {
            if (mailOrderPort.sendOrderCreateNotification(mailOrderCreateDTOS))
                log.info("Sending Order Create Notification Successful");

        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }
}
