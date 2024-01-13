package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.domain.models.Desist;
import com.subocol.manage.purchase.infrastructure.persistence.entities.DesistModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.DesistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DesistAdapterTest {

    @Mock
    private DesistRepository repository;

    @InjectMocks
    private DesistAdapter desistAdapter;

//    @Mock
//    private EntityManager entityManager;

    private Desist desist;

    private DesistModel desistModel;

    @BeforeEach
    void setup() {

        desist = Desist.builder().causal("Precio mal cotizado.").idOrder(15L).idProductOrder(28L).observation("Se valoro como preventivo pero definitivamente no esta averiado").build();

        desistModel = DesistModel.builder().causal("Precio mal cotizado.").idOrder(15L).idProductOrder(28L).observation("Se valoro como preventivo pero definitivamente no esta averiado").build();

    }


    @Test
    void testSave() {

        when(repository.save(any(DesistModel.class))).thenReturn(desistModel);

        Desist savedDesist = desistAdapter.save(desist);

        try {
            //Assertions.assertEquals for all entity attributes
            AttributeAssertions.assertAttributesEqual(desist, savedDesist);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

//    @Test
//    void testSaveAllNative() {
//
//        when(repository.save(any(DesistModel.class))).thenReturn(desistModel);
//
//        int savedDesist = desistAdapter.saveAllNative(List.of(desist));
//
//        try {
//            //Assertions.assertEquals for all entity attributes
//            AttributeAssertions.assertAttributesEqual(desist, savedDesist);
//        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//            System.out.println("Error cause: " + e.getMessage());
//        }
//
//    }
}
