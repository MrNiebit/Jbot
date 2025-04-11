package x.ovo.jbot.plugin.cloudpan.impl;

import org.junit.jupiter.api.Test;
import x.ovo.jbot.plugin.model.SubscribeModel;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h2>  </h2>
 *
 * @description:
 * @menu
 * @author: gitsilence
 * @description:
 * @date: 2025/4/10 23:52
 **/
class AliPanResourceSubscriberImplTest {

    @Test
    public void getShareId() {
        var url = "https://www.alipan.com/s/v1bBBEcNf9p/folder/67cd7c007ab50c0ee60c4f6187c1cd10117c33d2";
        String[] split = url.split("/");
        System.out.println(Arrays.toString(split));
        System.out.println(split[4]);
    }

    @Test
    public void listTest() {
        var url = "https://www.alipan.com/s/v1bBBEcNf9p/folder/67cd7c007ab50c0ee60c4f6187c1cd10117c33d2";
        SubscribeModel subscribeModel = new SubscribeModel();
        subscribeModel.setResourceUrl(url);
        AliPanResourceSubscriberImpl subscriber = new AliPanResourceSubscriberImpl();
        subscriber.checkForUpdate(subscribeModel);
    }

}