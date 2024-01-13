package com.subocol.manage.purchase.domain.models;

import java.sql.Timestamp;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain=true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManualPurchase 
{
  private Long id;

  private String externalEvent;

  private String brand;

  private String line;

  private String plate;

  private String description;

  private Integer quantity;

  private String reference;

  private String suggestedReference;

  private String status;

  private Timestamp date;

  private String cause;

  private Integer position;

  private Long eventId;

  private boolean auth;

  private boolean deleted;

  private boolean purchaseSubsidiary;

}

