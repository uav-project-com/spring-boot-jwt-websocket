package murraco.controller;

import murraco.model.EchoModel;
import murraco.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Naik on 23.02.17.
 */
public class SocketController {
    private static final Logger log = LoggerFactory.getLogger(SocketController.class);

    @MessageMapping("/hello-msg-mapping")
    @SendTo("/topic/greetings")
    public EchoModel echoMessageMapping(String message) {
        log.debug("React to hello-msg-mapping");
        return new EchoModel("Server answer: " + message.trim());
    }

    @RequestMapping(value = "/hello-convert-and-send", method = RequestMethod.POST)
    public void echoConvertAndSend(@RequestParam("msg") String message) {
        log.error("Message received: {}", message);
        socketService.echoMessage(message);
    }

    public SocketService getSocketService() {
        return socketService;
    }

    public void setSocketService(SocketService socketService) {
        this.socketService = socketService;
    }

    @Autowired
    private SocketService socketService;
}
