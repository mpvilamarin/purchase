package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_SELL_ORDER, schema = DataBase.Constant.SCH_PROVIDER)
public class SellOrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "last_update_date")
    private Timestamp lastUpdateDate;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "iva", nullable = false)
    private Double iva;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderModel order;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sellOrder", fetch = FetchType.EAGER)
    private Set<SellOrderDetailModel> details;

}