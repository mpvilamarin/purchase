package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

@ToString
@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusParts implements Serializable{

	private static final long serialVersionUID = -5347753318875955006L;

	private Long id;

	private StatusReplacement statusReplacement;

	//private Long statusReplacementId;

	private String namePart;

	private String reference;

	private Boolean importPart;

	private Integer totalOrdersEvent;

	private Long idOrder;

	private Long idProductOrder;

	private Integer totalParts;

	private String status;

	private Timestamp approvedOrderDate;

	private Timestamp estimateDeliveryDate;

	private Timestamp deliveredDate;

	private Timestamp onRouteDate;

	private Timestamp devolutionDate;

	private Timestamp lateDate;

	private Timestamp completedDate;

	private Timestamp receivedWorkshopDate;
	
//	@Column(name="desist_date")
//	private Timestamp desistDate;
		
}
