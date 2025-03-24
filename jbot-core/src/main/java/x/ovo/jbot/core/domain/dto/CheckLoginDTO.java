package x.ovo.jbot.core.domain.dto;

import lombok.Data;

/**
 * 检查登录 DTO
 *
 * @author ovo created on 2025/02/25.
 */
@Data
public class CheckLoginDTO {

    private String uuid;
    private String headImgUrl;
    private String nickName;
    private Integer expiredTime;
    private Integer status;
    private LoginInfo loginInfo;

    @Data
    public static class LoginInfo{
        private Integer uin;
        private String wxid;
        private String nickName;
        private String alias;
        private String mobile;
    }

}
