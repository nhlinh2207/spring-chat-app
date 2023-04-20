package com.example.chatapp.api;

import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.model.ConnectedUser;
import com.example.chatapp.model.Message;
import com.example.chatapp.repository.IUserRepo;
import com.example.chatapp.service.IChatRoomService;
import com.example.chatapp.service.IMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class ChatRoomController {

    private final IChatRoomService chatRoomService;
    private final IMessageService messageService;

    @Secured("ROLE_ADMIN")
    @PostMapping(path = "/chatroom")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public ChatRoom createChatRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomService.save(chatRoom);
    }

    @RequestMapping("/chatroom/{chatRoomId}")
    public ModelAndView join(@PathVariable String chatRoomId, Principal principal) {
        System.out.println(principal.getName());
        ModelAndView modelAndView = new ModelAndView("chatroom");
        modelAndView.addObject("chatRoom", chatRoomService.findById(Integer.valueOf(chatRoomId)));
        return modelAndView;
    }

    @SubscribeMapping("/connected.users")
    public List<ConnectedUser> listChatRoomConnectedUsersOnSubscribe(SimpMessageHeaderAccessor headerAccessor) throws JsonProcessingException {
        String chatRoomId = headerAccessor.getSessionAttributes().get("chatRoomId").toString();
        ChatRoom chatRoom = chatRoomService.findById(Integer.valueOf(chatRoomId));
        return new ObjectMapper().readValue(chatRoom.getConnectedUsers(), ArrayList.class);
    }

    @SubscribeMapping("/old.messages")
    public List<Message> listOldMessagesFromUserOnSubscribe(Principal principal,
                                                            SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor.getSessionAttributes().get("chatRoomId").toString();
        return messageService.findAllInstantMessagesFor(principal.getName(), chatRoomId);
    }

    @MessageMapping("/send.message")
    public void sendMessage(@Payload Message instantMessage, Principal principal,
                            SimpMessageHeaderAccessor headerAccessor) throws JsonProcessingException {
        String chatRoomId = headerAccessor.getSessionAttributes().get("chatRoomId").toString();
        instantMessage.setFromUser(principal.getName());
        instantMessage.setChatRoomId(Integer.valueOf(chatRoomId));

        if (instantMessage.isPublic()) {
            System.out.println("Public from : "+instantMessage.getFromUser());
            chatRoomService.sendPublicMessage(instantMessage);
        } else {
            System.out.println(instantMessage.getFromUser()+"  -->  "+instantMessage.getToUser());
            chatRoomService.sendPrivateMessage(instantMessage);
        }
    }
}
