package com.amolli.oyeongshop.ver2.order.controller;

import com.amolli.oyeongshop.ver2.order.model.Order;
import com.amolli.oyeongshop.ver2.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orderlist")
    public String orderList(){
        return "/order/order-list";
    }

    @GetMapping("/returnlist")
    public String returnList(){
        return "/order/return-list";
    }

    @GetMapping("/orderadd")
    public String orderAdd(Model model){
        model.addAttribute("order", Order.builder().build());
        return "/order/order";
    }

    @PostMapping("/orderadd")
    public String orderSave(@Validated Order order, BindingResult result){
        if(result.hasErrors()){
            System.out.println(order);
            System.out.println(result);

            return "/login";
        }else {
            Order savedOrder = orderService.save(order);

            System.out.println(order);
            System.out.println(result);
            return "/";
        }
        //결제랑 연결하기 전에 우선 테스트하는 동안은 메인으로 이동
    }

    @GetMapping("/orderDetail")
    public String orderDetail(){

        return "/order/order-detail";
    }
}