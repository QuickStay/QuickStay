package com.project.quickstay.controller;

import com.project.quickstay.common.Login;
import com.project.quickstay.domain.place.dto.MyPlaceInfo;
import com.project.quickstay.domain.place.dto.PlaceMiniInfo;
import com.project.quickstay.domain.room.dto.MyRoomInfo;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.service.PlaceService;
import com.project.quickstay.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PlaceService placeService;
    private final RoomService roomService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("mostReserved" , placeService.findFiveMostReservedPlace());
        model.addAttribute("highestReviewAverage" , placeService.findFiveHighestReviewAveragePlace());
        model.addAttribute("random" , placeService.findFiveRandomPlace());
        model.addAttribute("todayMostReserved" , placeService.findTenTodayMostReservedPlace());
        return "main";
    }

    @GetMapping("/myPage")
    public String myPage() {
        return "myPage/myPage";
    }

    @GetMapping("/myPage/place")
    public String myPlace(@Login User user, Model model) {
        List<MyPlaceInfo> myPlace = placeService.getMyPlace(user);
        model.addAttribute("myPlace", myPlace);
        return "myPage/myPlace";
    }

    @GetMapping("/myPage/{placeId}/rooms")
    public String myRoom(@PathVariable Long placeId, Model model) {
        List<MyRoomInfo> myRoom = roomService.getMyRoom(placeId);
        model.addAttribute("myRoom", myRoom);
        return "myPage/myRoom";
    }
}
