package com.wifiestastripe.billing.util;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class LocalDateSerializer {

	public static void main(String[] args) {
		LocalDate startDate = LocalDate.of(2020, 3, 1);
		LocalDate endDate = LocalDate.of(2020, 4, 1);
		
		
		System.out.println("startDate "+ startDate +" -> "+startDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
		System.out.println("endDate "+endDate+" -> "+endDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
	}

}
