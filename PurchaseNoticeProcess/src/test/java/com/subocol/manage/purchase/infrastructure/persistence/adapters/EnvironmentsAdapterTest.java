package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.infrastructure.persistence.entities.EnvironmentsModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.EnvironmentsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvironmentsAdapterTest {

    @Mock
    private EnvironmentsRepository repository;

    @InjectMocks
    private EnvironmentsAdapter environmentsAdapter;

    @Test
    void testFindByIdReturnEntity() {

        Long id = 999999999L;
        String environments = "this is my environment";
        EnvironmentsModel environmentsModel = EnvironmentsModel.builder()
                .id(id)
                .environments(environments)
                .build();


        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(environmentsModel));

        String result = environmentsAdapter.findEnvironmentsById(id);

        verify(repository, times(1)).findById(id);

        assertThat(result).isNotNull();
        Assertions.assertEquals(environments, result);

    }

}