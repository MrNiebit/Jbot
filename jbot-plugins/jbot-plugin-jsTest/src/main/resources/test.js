// noinspection JSUnresolvedReference

import {Listener} from "listener.js";

(() => {
    log.info("into test.js")
    const Plugin = Java.extend(PluginClass, {
        onLoad:() => log.info("onLoad"),
        onUnload:() => log.info("onUnload"),
        getEventListener: () => {
            return new Listener(this);
        },
        getCommandExecutor:() => null
    })
    log.info("new Plugin()")
    return new Plugin();

})()