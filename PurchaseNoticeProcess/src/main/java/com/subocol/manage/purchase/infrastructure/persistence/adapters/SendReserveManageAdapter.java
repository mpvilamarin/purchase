package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.SendReserveManage;
import com.subocol.manage.purchase.domain.ports.persistence.SendReserveManageRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SendReserveManageModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SendReserveManageRepository;

import java.util.Optional;

@Adapter
public class SendReserveManageAdapter implements SendReserveManageRepositoryPort {

    private final SendReserveManageRepository sendReserveManageRepository;

    public SendReserveManageAdapter(SendReserveManageRepository sendReserveManageRepository) {
        this.sendReserveManageRepository = sendReserveManageRepository;
    }

    @Override
    public Optional<SendReserveManage> findByExternalEvent(Integer externalEvent) {
        return sendReserveManageRepository.findByExternalEvent(externalEvent)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, SendReserveManage.class)));

    }

    @Override
    public SendReserveManage save(SendReserveManage sendReserveManage) {
        SendReserveManageModel sendReserveManageSaved = sendReserveManageRepository.save(MapperUtil.convert(sendReserveManage , SendReserveManageModel.class));
        return MapperUtil.convert(sendReserveManageSaved, SendReserveManage.class);
    }

}
