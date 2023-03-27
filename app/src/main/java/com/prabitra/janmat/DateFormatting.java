package com.prabitra.janmat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatting {
        public static String getFormattedDate(long time){
                String res = "";

                // Finding difference between times
                long currTime = System.currentTimeMillis();
                long diffTime = currTime - time;

                if(diffTime<60000){
                        res = "Less than a minute";
                }
                else if(diffTime<3600000){
                        int min = (int) (diffTime/60000);
                        if(min!=1) {
                                res = min + " minutes ago";
                        }
                        else{
                                res = min + " minute ago";
                        }
                }
                else if(diffTime<86400000){
                        int hour = (int) (diffTime/3600000);
                        if(hour!=1) {
                                res = hour + " hours ago";
                        }
                        else{
                                res = hour + " hour ago";
                        }
                }
                else if(diffTime<4*86400000){
                        int days = (int) (diffTime/86400000);
                        if(days==1){
                                res = days + " day ago";
                        }
                        else{
                                res = days + " days ago";
                        }
                }
                else{
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM");
                        Date result = new Date(time);
                        res = dateFormat.format(result);
                }

                return res;
        }

        public static String getFormattedDateWithYear(long time){
                String res = "";

                // Finding difference between times
                long currTime = System.currentTimeMillis();
                long diffTime = currTime - time;

                if(diffTime<60000){
                        res = "Less than a minute";
                }
                else if(diffTime<3600000){
                        int min = (int) (diffTime/60000);
                        if(min!=1) {
                                res = min + " minutes ago";
                        }
                        else{
                                res = min + " minute ago";
                        }
                }
                else if(diffTime<86400000){
                        int hour = (int) (diffTime/3600000);
                        if(hour!=1) {
                                res = hour + " hours ago";
                        }
                        else{
                                res = hour + " hour ago";
                        }
                }
                else if(diffTime<2*86400000){
                        int days = (int) (diffTime/86400000);
                        if(days==1){
                                res = days + " day ago";
                        }
                        else{
                                res = days + " days ago";
                        }
                }
                else{
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date result = new Date(time);
                        res = dateFormat.format(result);
                }

                return res;
        }
        }
