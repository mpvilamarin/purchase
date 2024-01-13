package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * Entity representing subsidiary data
 */

@Setter
@Getter
@Accessors(chain=true)
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Subsidiary 
{

	private Long id;

	private Provider provider;

	private String alias;

	private String email;

	private String name;

	private String phone;

	private Boolean status;

	private Long locationExternalId;

	private Integer warehouseIdDms;

	private String classification;

	private String idJob;

	private Long dmsCode;

	private Integer intermediation;

}
