package x.ovo.jbot.plugin.enmus;

/**
 * <h2>  </h2>
 *
 * @description:
 * @menu
 * @author: gitsilence
 * @description:
 * @date: 2025/4/10 10:47
 **/
public enum CloudPanEnums {

    ALI_PAN, BAIDU_PAN
    ;

    public static CloudPanEnums findByName(String name) {
        switch (name) {
            case "ali_pan":
                return ALI_PAN;
            case "baidu_pan":
                return BAIDU_PAN;
            default:
                return ALI_PAN;
        }
    }
}
