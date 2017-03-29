package cn.net.bjsoft.sxdz.utils.function;

/**
 * 此类用于计算时间的工具类
 * <p>
 * Created 靳宁宁 Zrzc on 2017/2/24.
 */

public class TimeCounter {

    public static int getDays(String startDay, String endDay, String decollator) {
        if (!startDay.equals("") && !endDay.equals("")) {//只要一个为空就不具备可比性,直接返回0
            String str_stat_days = "";
            String str_end_days = "";
            String str_start_month = "";
            String str_end_month = "";
            //开始时间的自然年月日
            int start_year = 0;
            int start_month = 0;
            int start_day = 0;
            //结束时间的自然年月日
            int end_year = 0;
            int end_month = 0;
            int end_day = 0;

            //开始时间的年月日的天数
            int day_start_year = 0;
            int day_start_month = 0;
            int day_start_day = 0;
            //结束时间的年月日的天数
            int day_end_year = 0;
            int day_end_month = 0;
            int day_end_day = 0;

            //开始时间截取字符串,将其标准化为int类型
            str_stat_days = startDay;
            start_year = Integer.parseInt(str_stat_days.substring(0, str_stat_days.indexOf(decollator)));

            str_stat_days = startDay;
            str_start_month = str_stat_days.substring(str_stat_days.indexOf(decollator), str_stat_days.lastIndexOf(decollator));
            if (str_start_month.startsWith("0")) {
                start_month = Integer.parseInt(str_start_month.replace("0", ""));
            } else {
                start_month = Integer.parseInt(str_start_month);
            }

            str_stat_days = startDay;
            start_day = Integer.parseInt(str_stat_days.substring(str_stat_days.lastIndexOf(decollator)));


            //结束时间截取字符串,将其标准化为int类型
            str_end_days = endDay;
            end_year = Integer.parseInt(str_end_days.substring(0, str_end_days.indexOf(decollator)));

            str_end_days = startDay;
            str_end_month = str_end_days.substring(str_end_days.indexOf(decollator), str_end_days.lastIndexOf(decollator));
            if (str_end_month.startsWith("0")) {
                end_month = Integer.parseInt(str_end_month.replace("0", ""));
            } else {
                end_month = Integer.parseInt(str_end_month);
            }

            str_end_days = endDay;
            end_day = Integer.parseInt(str_end_days.substring(str_end_days.lastIndexOf(decollator)));

            /**
             * 计算年的天数
             */
            if (start_year % 4 == 0) {
                day_start_year = 366;
            } else {
                day_start_year = 365;
            }

            if (end_year % 4 == 0) {
                day_end_year = 366;
            } else {
                day_end_year = 365;
            }

            /**
             * 计算月的天数
             */
            day_start_month = getMonthDays(start_year, start_month);
            day_end_month = getMonthDays(end_year, end_month);

            /**
             * 计算天的天数
             */
            day_start_day = start_day;
            day_end_day = end_day;


            return (day_start_year + day_start_month + day_start_day) - (day_end_year + day_end_month + day_end_day) + 1;
        } else return 0;
    }

    /**
     * 计算月的天数
     *
     * @param day_year  自然年的年数
     * @param day_month 自然月的月数
     * @return
     */
    private static int getMonthDays(int day_year, int day_month) {

        int days = 0;
        for (int month = 1; month <= day_month; month++) {
            switch (month) {
                case 1:
                    days = days + 31;
                    break;
                case 2:
                    if (day_year % 4 == 0) {
                        days = days + 29;
                    } else {
                        days = days + 28;
                    }
                    break;
                case 3:
                    days = days + 31;
                    break;
                case 4:
                    days = days + 30;
                    break;
                case 5:
                    days = days + 31;
                    break;
                case 6:
                    days = days + 30;
                    break;
                case 7:
                    days = days + 31;
                    break;
                case 8:
                    days = days + 31;
                    break;
                case 9:
                    days = days + 30;
                    break;
                case 10:
                    days = days + 31;
                    break;
                case 11:
                    days = days + 30;
                    break;
                case 12:
                    days = days + 31;
                    break;
            }
        }
        return days;
    }
}
