package x.ovo.jbot.core.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import x.ovo.jbot.core.contact.Contactable;

import java.util.Collection;

/**
 * 联系人相关服务
 *
 * @author ovo created on 2025/02/24.
 */
public interface ContactService {

    /**
     * 获取联系人列表
     *
     * @return {@link Collection }<{@link Contactable }>
     */
    Future<Collection<Contactable>> list();

    /**
     * 搜索联系人
     *
     * @param info 搜索的联系人信息，微信号、手机号...
     * @return {@link Contactable }
     */
    Future<JsonObject> search(String info);

    /**
     * 批量获取联系人信息
     *
     * @param ids id集合
     * @return {@link Collection }<{@link Contactable }>
     */
    Future<Collection<Contactable>> getInfo(Collection<String> ids);

    /**
     * 添加联系人/同意好友请求
     *
     * @param scene     添加来源，同意好友请求时回传xml中的scene。3-微信号搜索，4-QQ好友，8-来自群聊，10-手机号
     * @param operation 操作，2-添加好友，3-同意好友请求，4-拒绝好友请求
     * @param v3        通过搜索或回调消息获取到的v3
     * @param v4        通过搜索或回调消息获取到的v4
     * @param content   添加好友时的招呼语
     * @return {@link Contactable }
     */
    Future<Boolean> add(int scene, int operation, String v3, String v4, String content);

    /**
     * 删除好友
     * @param id id
     * @return boolean
     */
    Future<Boolean> delete(String id);

    /**
     * 设置备注
     *
     * @param id     id
     * @param remark 备注
     * @return boolean
     */
    Future<Boolean> setRemark(String id, String remark);

    /**
     * 设置好友权限
     *
     * @param id       id
     * @param chatOnly 仅聊天
     * @return boolean
     */
    Future<Boolean> setPermission(String id, boolean chatOnly);
}
