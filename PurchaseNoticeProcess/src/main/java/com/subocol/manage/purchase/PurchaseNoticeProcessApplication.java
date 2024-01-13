package com.subocol.manage.purchase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PurchaseNoticeProcessApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchaseNoticeProcessApplication.class, args);
	}

}
