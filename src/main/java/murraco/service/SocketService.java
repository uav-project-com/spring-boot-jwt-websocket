package murraco.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import murraco.model.Command;
import murraco.model.EchoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

/**
 * Created by Naik on 23.02.17.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {
    public void echoMessage(String message) {
        log.info("Start convertAndSend {}", new Date());
        simpTemplate.convertAndSend("/topic/greetings", new EchoModel(message));
        log.info("End convertAndSend {}", new Date());
    }

    /**
     * Send message forward to special user with username
     * client will subscribe: "/chat/queue/{username}/chat/queue/messages"
     * @param username send to
     * @param cm content data
     */
    public void forwardCommand(String username, Command cm) {
        simpTemplate.convertAndSendToUser(username, "/chat/queue/messages", cm);
    }

    private final SimpMessagingTemplate simpTemplate;
}
