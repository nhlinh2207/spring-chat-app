package com.example.chatapp.api;

import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.service.IChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
public class ChatController {

    private final IChatRoomService chatRoomService;

    @RequestMapping("/chat")
    public ModelAndView getRooms() {
        ModelAndView modelAndView = new ModelAndView("chat");
        List<ChatRoom> chatRooms = chatRoomService.findAll();
        modelAndView.addObject("chatRooms", chatRooms);
        return modelAndView;
    }

}
