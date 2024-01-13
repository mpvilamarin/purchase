package com.subocol.manage.purchase.domain.integrations;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.models.Subsidiary;
import com.subocol.manage.purchase.domain.ports.externalservices.MailOrderPort;
import com.subocol.manage.purchase.domain.ports.persistence.EnvironmentsRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.SubsidiaryRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.MailOrderCreateDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.MailOrderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_CREATING_SEND_MAIL_ORDER_DTO;
import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_SEND_MAIL_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailOrderTest {

    @Mock
    private MailOrderPort mailOrderPort;

    @Mock
    private EnvironmentsRepositoryPort environmentsRepositoryPort;

    @Mock
    private SubsidiaryRepositoryPort subsidiaryRepositoryPort;

    @InjectMocks
    private MailOrderImpl mailOrder;


    private List<MailOrderCreateDTO> mailOrderCreateDTOS;
    private Subsidiary subsidiary;

    @BeforeEach
    void setup() {
        MailOrderCreateDTO mailOrderCreateDTO = MailOrderCreateDTO.builder()
                .orderId(999999999L)
                .aviso("999999999")
                .email("test@test.com")
                .plate("XYZ123")
                .insurerName("SURA")
                .build();

        this.mailOrderCreateDTOS = new ArrayList<>();
        mailOrderCreateDTOS.add(mailOrderCreateDTO);

        this.subsidiary = new Subsidiary()
                .setId(999999999L)
                .setName("Subsidiary name")
                .setEmail("subsidiarytest@test.com");

    }

    @Test
    void testAddMailOrderNotificationSuccess() {

        when(subsidiaryRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.of(subsidiary));
        when(environmentsRepositoryPort.findEnvironmentsById(anyLong())).thenReturn("environments");

        mailOrder.addMailOrderNotification("99999999", 99999999L, 1, mailOrderCreateDTOS, "XYZ123", "SURA");

        verify(subsidiaryRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(environmentsRepositoryPort, times(1)).findEnvironmentsById(anyLong());

    }

    @Test
    void testAddMailOrderNotificationFailure() {

        when(subsidiaryRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.of(subsidiary));
        when(environmentsRepositoryPort.findEnvironmentsById(anyLong())).thenThrow(new NullPointerException());

         mailOrder.addMailOrderNotification("99999999", 99999999L, 1, mailOrderCreateDTOS, "XYZ123", "SURA");

        verify(subsidiaryRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(environmentsRepositoryPort, times(1)).findEnvironmentsById(anyLong());

//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getStatusCode());
//        assertEquals(ERROR_SEND_MAIL_ORDER, exception.getMessage());

    }

    @Test
    void testAddMailOrderNotificationSuccessWithoutMailOrders() {

        when(subsidiaryRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.of(subsidiary));
        when(environmentsRepositoryPort.findEnvironmentsById(anyLong())).thenReturn("environments");

        mailOrder.addMailOrderNotification("99999999", 99999999L, 1, null, "XYZ123", "SURA");

        verify(subsidiaryRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(environmentsRepositoryPort, times(1)).findEnvironmentsById(anyLong());

    }

    @Test
    void testAddMailOrderNotificationSuccessWithoutSubsidiary() {

        when(subsidiaryRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.empty());

        mailOrder.addMailOrderNotification("99999999", 99999999L, 1, null, "XYZ123", "SURA");

        verify(subsidiaryRepositoryPort, times(1)).findByOrderId(anyLong());

    }

//    @Test
//    void testCreateMailOrderDTOFailure() {
//
//        when(environmentsRepositoryPort.findEnvironmentsById(anyLong())).thenThrow(new NullPointerException());
//
//        Executable invocation = () -> mailOrder.createMailOrderDTO("99999999", subsidiary, 999999999L, true);
//
//        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);
//
//        verify(environmentsRepositoryPort, times(1)).findEnvironmentsById(anyLong());
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getStatusCode());
//        assertEquals(ERROR_CREATING_SEND_MAIL_ORDER_DTO, exception.getMessage());
//
//    }

    @Test
    void testCreateMailOrderDTOSuccess() {

        MailOrderCreateDTO result = mailOrder.createMailOrderDTO("99999999", subsidiary, 999999999L, true, "XYZ123", "SURA");

        assertThat(result).isNotNull();

    }

    @Test
    void testCreateMailOrderDTOValidateExternalEvent() {

        Executable invocation = () -> mailOrder.createMailOrderDTO(null, subsidiary, 999999999L, true, "XYZ123", "SURA");

        RuntimeException exception = Assertions.assertThrows(NullPointerException.class, invocation);

        assertEquals("externalEvent must not be null", exception.getMessage());

    }

    @Test
    void testCreateMailOrderDTOValidateSubsidiary() {

        Executable invocation = () -> mailOrder.createMailOrderDTO("999999999", null, 999999999L, true, "XYZ123", "SURA");

        RuntimeException exception = Assertions.assertThrows(NullPointerException.class, invocation);

        assertEquals("subsidiary must not be null", exception.getMessage());

    }


    @Test
    void testCreateMailOrderDTOValidateSubsidiaryName() {

        subsidiary.setName(null);
        Executable invocation = () -> mailOrder.createMailOrderDTO("999999999", subsidiary, 999999999L, true, "XYZ123", "SURA");

        RuntimeException exception = Assertions.assertThrows(NullPointerException.class, invocation);

        assertEquals("subsidiaryName must not be null", exception.getMessage());

    }


    @Test
    void testCreateMailOrderDTOValidateSubsidiaryEmail() {

        subsidiary.setEmail(null);
        Executable invocation = () -> mailOrder.createMailOrderDTO("999999999", subsidiary, 999999999L, true, "XYZ123", "SURA");

        RuntimeException exception = Assertions.assertThrows(NullPointerException.class, invocation);

        assertEquals("email must not be null", exception.getMessage());

    }

    @Test
    void testCreateMailOrderDTOValidateSubsidiaryId() {

        subsidiary.setId(null);
        Executable invocation = () -> mailOrder.createMailOrderDTO("999999999", subsidiary, 999999999L, true, "XYZ123", "SURA");

        RuntimeException exception = Assertions.assertThrows(NullPointerException.class, invocation);

        assertEquals("subsidiaryId must not be null", exception.getMessage());

    }

    @Test
    void testCreateMailOrderDTOValidateOrderId() {

        Executable invocation = () -> mailOrder.createMailOrderDTO("999999999", subsidiary, null, true, "XYZ123", "SURA");

        RuntimeException exception = Assertions.assertThrows(NullPointerException.class, invocation);

        assertEquals("orderId must not be null", exception.getMessage());

    }

    @Test
    void testCreateMailOrderDTOValidateManual() {

        Executable invocation = () -> mailOrder.createMailOrderDTO("99999999", subsidiary, 999999999L, null, "XYZ123", "SURA");

        RuntimeException exception = Assertions.assertThrows(NullPointerException.class, invocation);

        assertEquals("manual must not be null", exception.getMessage());

    }

    @Test
    void testCreateMailOrderDTOValidatePlate() {

        Executable invocation = () -> mailOrder.createMailOrderDTO("99999999", subsidiary, 999999999L, true, null, "SURA");

        RuntimeException exception = Assertions.assertThrows(NullPointerException.class, invocation);

        assertEquals("plate must not be null", exception.getMessage());

    }

    @Test
    void testCreateMailOrderDTOValidateInsurerName() {

        Executable invocation = () -> mailOrder.createMailOrderDTO("99999999", subsidiary, 999999999L, true, "XYZ123", null);

        RuntimeException exception = Assertions.assertThrows(NullPointerException.class, invocation);

        assertEquals("insurerName must not be null", exception.getMessage());

    }

    @Test
    void testSendMailOrderNotificationSuccess() {

        when(mailOrderPort.sendOrderCreateNotification(anyList())).thenReturn(Boolean.TRUE);

        mailOrder.sendMailOrderNotification(mailOrderCreateDTOS);

        verify(mailOrderPort, times(1)).sendOrderCreateNotification(anyList());

    }

    @Test
    void testSendMailOrderNotificationSuccessWithoutSend() {

        when(mailOrderPort.sendOrderCreateNotification(anyList())).thenReturn(Boolean.FALSE);

        mailOrder.sendMailOrderNotification(mailOrderCreateDTOS);

        verify(mailOrderPort, times(1)).sendOrderCreateNotification(anyList());

    }

    @Test
    void testSendMailOrderNotificationFailure() {
        
        when(mailOrderPort.sendOrderCreateNotification(anyList())).thenThrow(new NullPointerException());

//        Executable invocation = () -> mailOrder.sendMailOrderNotification(mailOrderCreateDTOS);

        mailOrder.sendMailOrderNotification(mailOrderCreateDTOS);

        verify(mailOrderPort, times(1)).sendOrderCreateNotification(anyList());

//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getStatusCode());
//        assertEquals(ERROR_SEND_MAIL_ORDER, exception.getMessage());

    }


}
