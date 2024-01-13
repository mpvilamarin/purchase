package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_SEND_RESERVE_MANAGE, schema = DataBase.Constant.SCH_PROVIDER)
public class SendReserveManageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "external_event")
    private Integer externalEvent;

    @Column(name = "init_car_sended")
    private Boolean initCarSended;

    @Column(name="date", nullable = false)
    private Timestamp date;
}
