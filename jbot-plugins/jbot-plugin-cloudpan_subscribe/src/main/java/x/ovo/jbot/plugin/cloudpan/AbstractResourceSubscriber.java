package x.ovo.jbot.plugin.cloudpan;

import x.ovo.jbot.plugin.enmus.CloudPanEnums;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

/**
 * <h2>  </h2>
 *
 * @description:
 * @menu
 * @author: gitsilence
 * @description:
 * @date: 2025/4/10 10:41
 **/
public abstract class AbstractResourceSubscriber implements CloudResourceSubscriber{

    public void startSubscription(String resourceIdentifier, DayOfWeek updateDay, LocalTime updateTime, Duration checkInterval) {

    }

}
