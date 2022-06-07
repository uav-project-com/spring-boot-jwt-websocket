package murraco.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import murraco.model.Command;
import murraco.model.EchoModel;
import murraco.service.SocketService;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Naik on 23.02.17.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class SocketController {

    /**
     * Gửi mes qua socket từ android đến '/hello-msg-mapping' và gửi response trả lại android: /topic/greetings
     * Android: subscribed /topic/greetings
     * @param message mess from client
     * @return response to target
     */
    @MessageMapping("/hello-msg-mapping")
    @SendTo("/topic/greetings")
    public Command echoMessageMapping(Command message) {
        log.info("React to hello-msg-mapping");
        message.setData("Server received:" + message.getData());
        return message;
    }

    /**
     * Send message to special user
     * @param message stomp websocket message
     * @param chatMessage content of payload
     */
    @MessageMapping("/commander")
    public void sendingMessage(Message<Object> message, @Payload Command chatMessage) {
        String authenSender = chatMessage.getFromUser();
        log.info("COMMAND: {}", chatMessage);
        chatMessage.setFromUser(authenSender);
        String recipient = chatMessage.getToUser();
        if (authenSender.equals(recipient)) {
            // send to themself
            socketService.forwardCommand(authenSender, chatMessage);
        }
        socketService.forwardCommand(recipient, chatMessage);
    }

    @PostMapping(value = "/hello-convert-and-send")
    public void echoConvertAndSend(@RequestParam("msg") String message) {
        log.warn("Message received: {}", message);
        socketService.echoMessage(message);
    }


    private final SocketService socketService;
}
