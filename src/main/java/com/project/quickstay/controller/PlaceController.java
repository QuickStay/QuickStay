package com.project.quickstay.controller;

import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.common.Login;
import com.project.quickstay.domain.place.dto.PlaceInfo;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.dto.PlaceSearch;
import com.project.quickstay.domain.room.dto.RoomData;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.service.PlaceService;
import com.project.quickstay.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PlaceController {

    private final PlaceService placeService;
    private final RoomService roomService;

    @GetMapping("/place/register")
    public String placeRegisterForm(Model model) {
        model.addAttribute("placeRegister", new PlaceRegister());
        return "place/placeRegister";
    }

    @PostMapping("/place/register")
    public String placeRegister(@Valid PlaceRegister placeRegister, BindingResult bindingResult, @Login User user) {
        if (bindingResult.hasErrors()) {
            log.error("bindingResult = {}", bindingResult);
            return "place/placeRegister";
        }
        placeService.register(user, placeRegister);
        return "redirect:/myPage";
    }

    @GetMapping("/place/{placeId}/update")
    public String placeUpdateForm(@PathVariable Long placeId, Model model) {
        PlaceRegister updateData = placeService.getUpdateData(placeId);
        model.addAttribute("updateData", updateData);
        return "place/placeUpdate";
    }

    @PostMapping("/place/{placeId}/update")
    public String placeUpdate(@PathVariable Long placeId, @Login User user, @ModelAttribute("updateData") @Valid PlaceRegister placeRegister, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "place/placeUpdate";
        }

        placeService.update(user, placeId, placeRegister);
        return "redirect:/place/" + placeId + "/info";
    }

    @PostMapping("/place/{placeId}/delete")
    public String placeDelete(@PathVariable Long placeId, @Login User user) {
        placeService.delete(user, placeId);
        return "main";
    }

    @GetMapping("/place/{placeId}/info")
    public String placeInfo(@PathVariable Long placeId, Model model) {
        PlaceInfo info = placeService.info(placeId);
        model.addAttribute("info", info);
        return "place/placeInfo";
    }

    @GetMapping("/place/search")
    public String placeSearch(@RequestParam(required = false) Long last, @RequestParam String keyword, Model model) {
        List<PlaceSearch> search = placeService.search(keyword, last);
        model.addAttribute("search", search);
        return "place/placeSearch";
    }

    /**
     * Room
     */
    @GetMapping("/place/{placeId}/register")
    public String roomRegisterForm(@PathVariable Long placeId, Model model) {
        model.addAttribute("roomData", new RoomData());
        model.addAttribute("placeId", placeId);
        return "place/room/roomRegister";
    }

    @PostMapping("/place/{placeId}/register")
    public String roomRegister(@PathVariable Long placeId, @Valid RoomData roomData, BindingResult bindingResult, @Login User user) {
        if (bindingResult.hasErrors()) {
            log.error("bindingResult = {}", bindingResult);
            return "place/room/roomRegister";
        }

        if (roomData.getBookType() == BookType.TIME) {
            LocalTime startTime = roomData.getStartTime();
            LocalTime endTime = roomData.getEndTime();
            if (validTime(startTime, endTime)) {
                bindingResult.rejectValue("endTime", "Error.time");
                return "place/room/roomRegister";
            }
        }
        roomService.register(user, placeId, roomData);
        return "main";

    }

    @GetMapping("/room/{roomId}/update")
    public String roomUpdateForm(@PathVariable Long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        RoomData updateData = roomService.getUpdateData(roomId);
        model.addAttribute("updateData", updateData);
        return "place/room/roomUpdate";
    }

    @PostMapping("/room/{roomId}/update")
    public String roomUpdate(@PathVariable Long roomId, @ModelAttribute("updateData") RoomData update, BindingResult bindingResult, @Login User user) {
        if (update.getBookType() == BookType.TIME) {
            if (validTime(update.getStartTime(), update.getEndTime())) {
                bindingResult.rejectValue("endTime", "Error.time");
                return "place/room/roomUpdate";
            }
        }
        roomService.update(user, roomId, update);
        return "redirect:/myPage/place";
    }

    @PostMapping("/room/{roomId}/delete")
    public String roomDelete(@PathVariable Long roomId, @Login User user) {
        roomService.delete(user, roomId);
        return "redirect:/myPage/place";
    }

    //return true when time is wrong
    private boolean validTime(LocalTime startTime, LocalTime endTime) {
        if (endTime.isBefore(startTime) || endTime.equals(startTime) || startTime.plusHours(1).isAfter(endTime)) {
            /**
             * 1. endTime > startTime
             * 2. endTime = startTime
             * 3. startTime + 1hour > endTime //ex) startTime 7:00, endTime 7:30
             */
            return true;
        }
        return false;
    }
}
