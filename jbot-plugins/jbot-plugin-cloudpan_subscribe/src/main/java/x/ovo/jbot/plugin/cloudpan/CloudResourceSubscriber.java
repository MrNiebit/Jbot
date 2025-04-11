package x.ovo.jbot.plugin.cloudpan;

import x.ovo.jbot.plugin.model.SubscribeModel;

/**
 * <h2>  </h2>
 *
 * @description:
 * @menu
 * @author: gitsilence
 * @description:
 * @date: 2025/4/10 10:36
 **/
public interface CloudResourceSubscriber {


    /**
     * 检查是否有更新
     * @param subscribeModel
     * @return
     */
    String checkForUpdate(SubscribeModel subscribeModel); // 检查是否有更新

}
