package x.ovo.jbot.core.service;

import io.vertx.core.Future;
import x.ovo.jbot.core.contact.Friend;
import x.ovo.jbot.core.domain.dto.CheckLoginDTO;
import x.ovo.jbot.core.domain.dto.QrcodeDTO;

/**
 * 登录服务
 *
 * @author ovo created on 2025/02/23.
 */
public interface LoginService {

    Future<Void> login();

    /**
     * 获取二维码
     *
     * @return {@link Future }<{@link QrcodeDTO }>
     */
    Future<QrcodeDTO> getQrcode();

    /**
     * 检查登录
     *
     * @param uuid uuid
     * @return {@link Future }<{@link CheckLoginDTO }>
     */
    Future<CheckLoginDTO> checkLogin(String uuid);

    /**
     * 弹窗登录/唤醒
     *
     * @return {@link Future }<{@link Void }>
     */
    Future<String> dialogLogin();

    /**
     * 重新连接
     *
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> reConnect();

    /**
     * 退出登录
     *
     * @return {@link Future }<{@link Void }>
     */
    Future<Void> logout();

    /**
     * 查询在线状态
     *
     * @return {@link Future }<{@link Boolean }>
     */
    Future<Boolean> checkOnline();

    /**
     * 获取用户信息
     *
     * @return {@link Future }<{@link Friend }>
     */
    Future<Friend> getUserInfo();

}
