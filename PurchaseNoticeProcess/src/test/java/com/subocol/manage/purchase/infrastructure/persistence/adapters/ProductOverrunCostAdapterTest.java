package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.domain.models.ProductOverrunCost;
import com.subocol.manage.purchase.domain.models.Quotation;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOverrunCostModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductQuotationModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.QuotationModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ProductOverrunCostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductOverrunCostAdapterTest {

    @Mock
    private ProductOverrunCostRepository repository;

    @InjectMocks
    private ProductOverrunCostAdapter productOverrunCostAdapter;

    ProductQuotationModel productQuotation1;
    ProductQuotationModel productQuotation2;
    ProductOverrunCostModel productOverrunCostModel;
    ProductOverrunCost productOverrunCost;
    ProductOverrunCostModel productOverrunCostModelSaved;
    QuotationModel quotationModel;
    Quotation quotation;

    @BeforeEach
    void setup() {
        quotationModel = QuotationModel.builder().providerName("PROVEEDOR AUTOSUM DEV").nit("29122220537").replacementReference("No aplica")
                .unities(98).price(4650.0).quality("").importation(false).timeDelivery(0).observations("").brand("TOYOTA")
                .line("RAV4").city("PANAMA").status("quoted").externalEvent("79249777").time(new Timestamp(1691594464)).flowType("Automatico")
                .noticeId(29432L).unforeseen(false).repairOrder(BigDecimal.valueOf(6546544)).adiUpdated(false).dateUpdateQuotation(new Timestamp(6546544))
                .quotationManaged(true).quotationWinner(false).build();

        quotation = Quotation.builder().providerName("PROVEEDOR AUTOSUM DEV").nit("29122220537").replacementReference("No aplica")
                .unities(98).price(4650.0).quality("").importation(false).timeDelivery(0).observations("").brand("TOYOTA")
                .line("RAV4").city("PANAMA").status("quoted").externalEvent("79249777").time(new Timestamp(1691594464)).flowType("Automatico")
                .noticeId(29432L).unforeseen(false).repairOrder(BigDecimal.valueOf(6546544)).adiUpdated(false).dateUpdateQuotation(new Timestamp(6546544))
                .quotationManaged(true).quotationWinner(false).build();


        productQuotation1 = ProductQuotationModel.builder()
                .position(1).reference("ProductReference").netPrice(10.0).description("ProductDescription")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2)
                .discountCampaigns(1.0).discountAdditional(2.0)
                .discountManual(3.0).comment("ProductComment").build();

        productQuotation2 = ProductQuotationModel.builder()
                .position(1).reference("ProductReference").netPrice(10.0).description("ProductDescription")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2)
                .discountCampaigns(1.0).discountAdditional(2.0)
                .discountManual(3.0).comment("ProductComment").build();

        productOverrunCostModel = ProductOverrunCostModel.builder().externalEvent("54124").brand("KIA").line("Picanto").plate("ABC123")
                .description("tapa motor").quantity(2).reference("sin referencia").suggestedReference("").timeDelivery(2)
                .importer(false).valueExtraCost(200.0).extraCost(true).netPrice(1200.0).grossPrice(1200.0).discountAdditional(0.0)
                .discountBrand(0.0).discountCampaigns(0.0).discountManual(0.0).status("quoted").date(new Timestamp(1664579257)).maxDeliveryDays(false)
                .quality("original").comment("").pieceId(541L).quotation(quotationModel).build();

        productOverrunCost = ProductOverrunCost.builder().externalEvent("54124").brand("KIA").line("Picanto").plate("ABC123")
                .description("tapa motor").quantity(2).reference("sin referencia").suggestedReference("").timeDelivery(2)
                .importer(false).valueExtraCost(200.0).extraCost(true).netPrice(1200.0).grossPrice(1200.0).discountAdditional(0.0)
                .discountBrand(0.0).discountCampaigns(0.0).discountManual(0.0).status("quoted").date(new Timestamp(1664579257)).maxDeliveryDays(false)
                .quality("original").comment("").pieceId(541L).quotation(quotation).build();

        productOverrunCostModelSaved = ProductOverrunCostModel.builder().externalEvent("54124").brand("KIA").line("Picanto").plate("ABC123")
                .description("tapa motor").quantity(2).reference("sin referencia").suggestedReference("").timeDelivery(2)
                .importer(false).valueExtraCost(200.0).extraCost(true).netPrice(1200.0).grossPrice(1200.0).discountAdditional(0.0)
                .discountBrand(0.0).discountCampaigns(0.0).discountManual(0.0).status("quoted").date(new Timestamp(1664579257)).maxDeliveryDays(false)
                .quality("original").comment("").pieceId(541L).quotation(quotationModel).build();

    }


    @Test
    void productQuotationAdapter_updateActiveProductQuotation_ReturnsUpdatedCount() {

        int updatedCount = 3;
        List<Long> lsIdsProductQuotation = Arrays.asList(1L, 2L, 3L);
        when(repository.updatePurchaseByStatusAndIdIn(anyList())).thenReturn(updatedCount);

        int result = productOverrunCostAdapter.updatePurchaseByStatusAndIdIn(lsIdsProductQuotation);

        assertEquals(updatedCount, result);
    }

    @Test
    void findAllByPieceId_ReturnsRow() {
        Long id = 141L;
        List<ProductOverrunCostModel> lsIdsProductOverrunCost= Collections.singletonList(productOverrunCostModel);

        when(repository.findAllByPieceId(anyLong())).thenReturn(lsIdsProductOverrunCost);

        Optional<ProductOverrunCost> result = productOverrunCostAdapter.findAllByPieceId(id);

        assertNotNull(result);
        assertTrue(result.isPresent());
        verify(repository, times(1)).findAllByPieceId(id);
        verifyNoMoreInteractions(repository);

    }

    @Test
    void findAllByPieceId_ReturnsEmpty() {
        Long id = 141L;
        List<ProductOverrunCostModel> emptyList = Collections.emptyList();

        when(repository.findAllByPieceId(anyLong())).thenReturn(emptyList);

        Optional<ProductOverrunCost> result = productOverrunCostAdapter.findAllByPieceId(id);

        assertNotNull(result);
        assertFalse(result.isPresent());
        verify(repository, times(1)).findAllByPieceId(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testSave() {
        when(repository.save(any(ProductOverrunCostModel.class))).thenReturn(productOverrunCostModelSaved);

        ProductOverrunCost savedProductOverrunCost = productOverrunCostAdapter.save(productOverrunCost);

        try {
            AttributeAssertions.assertAttributesEqual(productOverrunCost, savedProductOverrunCost);
            AttributeAssertions.assertAttributesEqual(productOverrunCost.getQuotation(), savedProductOverrunCost.getQuotation());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

}
