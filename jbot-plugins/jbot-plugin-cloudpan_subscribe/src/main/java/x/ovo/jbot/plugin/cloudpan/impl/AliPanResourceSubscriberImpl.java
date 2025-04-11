package x.ovo.jbot.plugin.cloudpan.impl;

import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.json.JSONUtil;
import x.ovo.jbot.plugin.cloudpan.AbstractResourceSubscriber;
import x.ovo.jbot.plugin.model.CloudResourceInfoModel;
import x.ovo.jbot.plugin.model.SubscribeModel;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.dromara.hutool.http.HttpUtil.post;

/**
 * <h2>  </h2>
 *
 * @description:
 * @menu
 * @author: gitsilence
 * @description:
 * @date: 2025/4/10 10:45
 **/
@Slf4j
public class AliPanResourceSubscriberImpl extends AbstractResourceSubscriber {

    record ResourceInfo(String shareId, String parentFileId) {}


    @Override
    public String checkForUpdate(SubscribeModel subscribeModel) {
        // 首先判断当前时间是星期几，和subscribeModel中的是否一致
        // 当前时间
        LocalTime nowTime = LocalTime.now();
        DayOfWeek nowDay = DayOfWeek.from(java.time.LocalDate.now());
        // 获取今天的日期 yyyy-MM-dd
        String today = java.time.LocalDate.now().toString(); // yyyy-MM-dd 格式

        // 判断星期几是否一致
        if (!nowDay.equals(subscribeModel.getDayOfWeek())) {
            log.info("当前时间({})和订阅时间({})不一致，跳过执行", nowDay, subscribeModel.getDayOfWeek());
            subscribeModel.setProcessed(false);
            return "";
        }

        // 然后判断当前的时间和subscribeModel中的时间是否一致
        // 解析 cronTime（格式假设为 "HH:mm"）
        if (subscribeModel.getCronTime() == null || !subscribeModel.getCronTime().matches("\\d{1,2}:\\d{1,2}")) {
            log.warn("订阅时间格式不对，{}", subscribeModel.getCronTime());
            return "";
        }
        if (subscribeModel.isProcessed()) {
            log.info("{}, 今日已经执行过，跳过执行", subscribeModel.getResourceUrl());
            return "";
        }

        String[] parts = subscribeModel.getCronTime().split(":");
        int targetHour = Integer.parseInt(parts[0]);
        // 提前一个小时开始
        if (nowTime.getHour() - targetHour < -1) {
            log.warn("具体时间还没到，当前小时({}), 订阅小时({})", nowTime.getHour(), targetHour);
            return "";
        }

        // get share id
        Assert.notBlank(subscribeModel.getResourceUrl());
        ResourceInfo resourceInfo = getResourceInfo(subscribeModel.getResourceUrl());
        String shareId = resourceInfo.shareId;
        String parentFileId = resourceInfo.parentFileId;

        // get share token
        String shareToken = getShareToken(shareId);

        System.out.println("shareId: " + shareId + "; shareToken: " + shareToken);
        // list by share, 这里根据更新时间排序，取出最新的一条
        CloudResourceInfoModel resourceInfoModel = listByShare(shareToken, shareId, parentFileId);
        // check update
        // 对比下日期，是否是当天更新的
        if (!resourceInfoModel.getUpdatedAt().contains(today)) {
            log.info("今日没有更新");
            return "";
        }
        subscribeModel.setProcessed(true);

        return String.format("""
                - 订阅的影视有更新
                文件名：    %s
                更新时间： %s
                文件大小： %s MB
                链接地址： %s
                """, resourceInfoModel.getName(), resourceInfoModel.getUpdatedAt(), resourceInfoModel.getSize() / 1024 / 1024, subscribeModel.getResourceUrl());
    }

    private ResourceInfo getResourceInfo(String url) {
        String[] split = url.split("/");
        return new ResourceInfo(split[4], split[split.length - 1]);
    }

    private String getShareToken(String shareId) {
        String apiUrl = "https://api.aliyundrive.com/v2/share_link/get_share_token";
        String responseStr = post(apiUrl, "{\"share_pwd\": \"\",\"share_id\":\"" + shareId + "\"}");
        return Buffer.buffer(responseStr).toJsonObject().getString("share_token");
    }

    private CloudResourceInfoModel listByShare(String shareToken, String shareId, String parentFileId) {
        String apiUrl = "https://api.aliyundrive.com/adrive/v2/file/list_by_share";
        String requestBody = """
                {
                	"share_id": "%s",
                	"parent_file_id": "%s",
                	"limit": 20,
                	"image_thumbnail_process": "image/resize,w_256/format,jpeg",
                	"image_url_process": "image/resize,w_1920/format,jpeg/interlace,1",
                	"video_thumbnail_process": "video/snapshot,t_1000,f_jpg,ar_auto,w_256",
                	"order_by": "updated_at",
                	"order_direction": "DESC"
                }
                """;
        Request request = HttpUtil.createPost(apiUrl).header("x-share-token", shareToken)
                .body(String.format(requestBody, shareId, parentFileId));

        try (Response response = HttpUtil.send(request);) {
            // System.out.println(response.bodyStr());
            String responseStr = response.bodyStr();
            String items = JSONUtil.parseObj(responseStr).getStr("items");
            List<CloudResourceInfoModel> cloudResourceInfoModels = JSONUtil.toList(items, CloudResourceInfoModel.class);

            if (cloudResourceInfoModels == null || cloudResourceInfoModels.isEmpty()) {
                throw new RuntimeException("Response contains no items");
            }

            // 安全获取第一个元素
            CloudResourceInfoModel resourceInfoModel = cloudResourceInfoModels.get(0);
            if (resourceInfoModel == null) {
                throw new RuntimeException("First item in items array is null");
            }

            // 安全获取字段值，假设字段存在且类型正确
            // System.out.printf("""
            //         name:           %s
            //         更新时间：    %s
            //         大小：          %s MB
            //         %n""", resourceInfoModel.getName(), resourceInfoModel.getUpdatedAt(), resourceInfoModel.getSize() / 1024/1024);
            return resourceInfoModel;
        } catch (IOException e) {
            // 记录日志并抛出更具体异常
            throw new RuntimeException("HTTP request failed", e);
        }
    }
}
