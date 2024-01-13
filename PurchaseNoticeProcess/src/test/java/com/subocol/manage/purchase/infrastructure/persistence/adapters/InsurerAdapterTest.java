package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.domain.models.InsuranceCarrier;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.models.ManualPurchase;
import com.subocol.manage.purchase.infrastructure.persistence.entities.InsuranceCarrierModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.InsurerModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ManualPurchaseModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.InsuranceCarrierRepository;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.InsurerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsurerAdapterTest {


    @Mock
    private InsurerRepository repository;

    @InjectMocks
    private InsurerAdapter insurerAdapter;

    InsurerModel insurerModel;
    Long InsurerId = 500000002L;

    @BeforeEach
    void setup() {
        insurerModel = InsurerModel.builder().name("SURA").insurerId(500000002L).countryId(1L).sdkActive(false).multimedia(false).priceToUse("autosuministro/original/precio_maximo")
                .flowIdJob(false).sdkDmsSubsidiary(false).sdkDmsOrders(false).irsParameterManual(false).unregisteredSubsidiary(false).newSuggestedReferenceParameter(true)
                .prioritizePriceList(false).usePriceList(true).useSuggestedReference(true).lengthReference(35).dontAllowReferenceEmpty(true).useGrossPriceCostoverrun(true)
                .calculateValueCostOverrun(false).noHomologueManual(false).totManual(false).allowMaxCostPiece(true).useOrbikaValuation(true).ignoreMaxCostPieceMM(false)
                .daysUpdateSuggestedReference(7L).decimals(true).flowReserveBolivar(true).sendOrderEmailWinner(true).sendOrderFact(true).assignWorkshopId(true)
                .requiredReference(true).modalReferenceSwitch(false).qualityPieceQuotation(true).flowReserveSura(true).cantRefToShow(1).calculateValueCostOverrun(false).noHomologueManual(false).build();

    }

    @Test
    void insurerAdapter_findByInsurerId_ReturnInsurerEntity() {

        when(repository.findByInsurerId(anyLong())).thenReturn(Optional.of(insurerModel));

        Optional<Insurer> resultInsurer = insurerAdapter.findByInsurerId(InsurerId);

        assertThat(resultInsurer).isPresent();
        assertThat(resultInsurer.get()).isNotNull();
        assertThat(resultInsurer.get()).isExactlyInstanceOf(Insurer.class);

        Insurer domainInsurer = resultInsurer.get();

        assertThat(domainInsurer.getInsurerId()).isEqualTo(InsurerId);
        assertThat(domainInsurer).isNotNull();
        Assertions.assertEquals(domainInsurer.getId(), insurerModel.getId());
        Assertions.assertEquals(domainInsurer.getName(), insurerModel.getName());
        Assertions.assertEquals(domainInsurer.getInsurerId(), insurerModel.getInsurerId());
        Assertions.assertEquals(domainInsurer.getCountryId(), insurerModel.getCountryId());
        Assertions.assertEquals(domainInsurer.getSdkActive(), insurerModel.getSdkActive());
        Assertions.assertEquals(domainInsurer.getFlowIdJob(), insurerModel.getFlowIdJob());
        Assertions.assertEquals(domainInsurer.getPriceToUse(), insurerModel.getPriceToUse());
        Assertions.assertEquals(domainInsurer.getSdkDmsSubsidiary(), insurerModel.getSdkDmsSubsidiary());
        Assertions.assertEquals(domainInsurer.getSdkDmsOrders(), insurerModel.getSdkDmsOrders());
        Assertions.assertEquals(domainInsurer.getIrsParameterManual(), insurerModel.getIrsParameterManual());
        Assertions.assertEquals(domainInsurer.getUnregisteredSubsidiary(), insurerModel.getUnregisteredSubsidiary());
        Assertions.assertEquals(domainInsurer.isMultimedia(), insurerModel.isMultimedia());
        Assertions.assertEquals(domainInsurer.getNewSuggestedReferenceParameter(), insurerModel.getNewSuggestedReferenceParameter());
        Assertions.assertEquals(domainInsurer.getPrioritizePriceList(), insurerModel.getPrioritizePriceList());
        Assertions.assertEquals(domainInsurer.getUsePriceList(), insurerModel.getUsePriceList());
        Assertions.assertEquals(domainInsurer.getUseSuggestedReference(), insurerModel.getUseSuggestedReference());
        Assertions.assertEquals(domainInsurer.getLengthReference(), insurerModel.getLengthReference());
        Assertions.assertEquals(domainInsurer.getDontAllowReferenceEmpty(), insurerModel.getDontAllowReferenceEmpty());
        Assertions.assertEquals(domainInsurer.getUseGrossPriceCostoverrun(), insurerModel.getUseGrossPriceCostoverrun());
        Assertions.assertEquals(domainInsurer.getCalculateValueCostoverrun(), insurerModel.getCalculateValueCostOverrun());
        Assertions.assertEquals(domainInsurer.getNoHomologueManual(), insurerModel.getNoHomologueManual());
        Assertions.assertEquals(domainInsurer.getTotManual(), insurerModel.getTotManual());
        Assertions.assertEquals(domainInsurer.getAllowMaxCostPiece(), insurerModel.getAllowMaxCostPiece());
        Assertions.assertEquals(domainInsurer.getUseOrbikaValuation(), insurerModel.getUseOrbikaValuation());
        Assertions.assertEquals(domainInsurer.getIgnoreMaxcostpieceMM(), insurerModel.getIgnoreMaxCostPieceMM());
        Assertions.assertEquals(domainInsurer.getDaysUpdateSuggestedReference(), insurerModel.getDaysUpdateSuggestedReference());
        Assertions.assertEquals(domainInsurer.isDecimals(), insurerModel.isDecimals());
        Assertions.assertEquals(domainInsurer.getFlowReserveBolivar(), insurerModel.getFlowReserveBolivar());
        Assertions.assertEquals(domainInsurer.getSendOrderEmailWinner(), insurerModel.getSendOrderEmailWinner());
        Assertions.assertEquals(domainInsurer.getSendOrderFact(), insurerModel.getSendOrderFact());
        Assertions.assertEquals(domainInsurer.getAssignWorkshopId(), insurerModel.getAssignWorkshopId());
        Assertions.assertEquals(domainInsurer.getRequiredReference(), insurerModel.getRequiredReference());
        Assertions.assertEquals(domainInsurer.getModalReferenceSwitch(), insurerModel.getModalReferenceSwitch());
        Assertions.assertEquals(domainInsurer.getQualityPieceQuotation(), insurerModel.getQualityPieceQuotation());
        Assertions.assertEquals(domainInsurer.getFlowReserveSura(), insurerModel.getFlowReserveSura());
        Assertions.assertEquals(domainInsurer.getCantRefToShow(), insurerModel.getCantRefToShow());

    }


    @Test
    void InsurerAdapter_findByInsurerId_ReturnEmptyOptional() {

        when(repository.findByInsurerId(anyLong())).thenReturn(Optional.empty());

        Optional<Insurer> resultInsurer = insurerAdapter.findByInsurerId(InsurerId);

        assertThat(resultInsurer).isNotPresent();

    }
}
