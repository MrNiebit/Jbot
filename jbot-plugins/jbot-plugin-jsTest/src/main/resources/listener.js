// noinspection JSUnresolvedReference

log.info("into listener.js")
const Listener = Java.extend(EventListenerClass, {
    support(e, s){
        return true
    },
    onEvent(e, s) {
        log.info("onEvent: {}", s.getContent())
        return true;
    },
    executeNext() {
        return true;
    },
    getEventClass() {
        return Java.type("x.ovo.jbot.core.event.MessageEvent")
    },
    getSourceClass() {
        return Java.type("x.ovo.jbot.core.message.entity.TextMessage")
    }
})

export {Listener}