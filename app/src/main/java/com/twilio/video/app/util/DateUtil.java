package com.twilio.video.app.util;

public class DateUtil {

    public static String getDate(String date)
    {
        String[] dateAry = date.split("-");
        switch (dateAry[1])
        {
            case "01":
                return dateAry[2]+" "+"Jan";
            case "02":
                return dateAry[2]+" "+"Feb";

            case "03":
                return dateAry[2]+" "+"Mar";

            case "04":
                return dateAry[2]+" "+"Apr";

            case "05":
                return dateAry[2]+" "+"May";

            case "06":
                return dateAry[2]+" "+"Jun";

            case "07":
                return dateAry[2]+" "+"July";

            case "08":
                return dateAry[2]+" "+"Aug";

            case "09":
                return dateAry[2]+" "+"Sept";

            case "10":
                return dateAry[2]+" "+"Oct";

            case "11":
                return dateAry[2]+" "+"Nov";

            case "12":
                return dateAry[2]+" "+"Dec";

            default:
                return dateAry[2]+" "+"Month";


        }
    }
}
