package com.subocol.manage.purchase.domain.utils;

import java.util.Calendar;
import java.util.Date;

public class OrderUtil {

    public static Date plusBusinessDays(Date date, int businessDays) {
        Calendar fechaCalendario = Calendar.getInstance();
        fechaCalendario.setTime(date);
        int dias = 0;
        while (dias <= businessDays) {
            if ((fechaCalendario.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) && (fechaCalendario.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)) {

                date = fechaCalendario.getTime();
                dias++;
            }
            fechaCalendario.add(Calendar.DATE, 1);
        }
        return date;
    }
}
