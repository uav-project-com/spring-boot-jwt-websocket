package murraco.service;

import murraco.model.EchoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Naik on 23.02.17.
 */
@Service
public class SocketService {
    Logger log = LoggerFactory.getLogger(SocketService.class);
    public void echoMessage(String message) {
        log.debug("Start convertAndSend " + String.valueOf(new Date()));
        simpTemplate.convertAndSend("/topic/greetings", new EchoModel(message));
        log.debug("End convertAndSend " + String.valueOf(new Date()));
    }

    @Autowired
    private SimpMessagingTemplate simpTemplate;
}
