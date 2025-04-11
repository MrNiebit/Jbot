package x.ovo.jbot.plugin.util;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>  </h2>
 *
 * @description:
 * @menu
 * @author: gitsilence
 * @description:
 * @date: 2025/4/10 12:50
 **/
public class CronTimeParser {
    private static final Map<String, DayOfWeek> dayMap = new HashMap<>();
    private static final Pattern timePattern = Pattern.compile("(上午|下午|中午|晚上)?(\\d{1,2})点");

    static {
        dayMap.put("周一", DayOfWeek.MONDAY);
        dayMap.put("星期一", DayOfWeek.MONDAY);
        dayMap.put("周二", DayOfWeek.TUESDAY);
        dayMap.put("星期二", DayOfWeek.TUESDAY);
        dayMap.put("周三", DayOfWeek.WEDNESDAY);
        dayMap.put("星期三", DayOfWeek.WEDNESDAY);
        dayMap.put("周四", DayOfWeek.THURSDAY);
        dayMap.put("星期四", DayOfWeek.THURSDAY);
        dayMap.put("周五", DayOfWeek.FRIDAY);
        dayMap.put("星期五", DayOfWeek.FRIDAY);
        dayMap.put("周六", DayOfWeek.SATURDAY);
        dayMap.put("星期六", DayOfWeek.SATURDAY);
        dayMap.put("周日", DayOfWeek.SUNDAY);
        dayMap.put("星期日", DayOfWeek.SUNDAY);
        dayMap.put("星期天", DayOfWeek.SUNDAY);
    }

    public static CronTime parse(String input) {
        DayOfWeek day = null;
        for (var entry : dayMap.entrySet()) {
            if (input.contains(entry.getKey())) {
                day = entry.getValue();
                break;
            }
        }

        if (day == null) {
            throw new IllegalArgumentException("未能识别星期几: " + input);
        }

        Matcher matcher = timePattern.matcher(input);
        if (matcher.find()) {
            String period = matcher.group(1); // 上午/下午
            int hour = Integer.parseInt(matcher.group(2));

            if (period != null) {
                switch (period) {
                    case "下午":
                    case "晚上":
                        if (hour < 12) hour += 12;
                        break;
                    case "中午":
                        hour = 12;
                        break;
                    // 上午不变
                }
            }

            return new CronTime(day, hour + ":00");
        } else {
            throw new IllegalArgumentException("未能识别时间: " + input);
        }
    }

    public record CronTime(DayOfWeek dayOfWeek, String time) {}
}
