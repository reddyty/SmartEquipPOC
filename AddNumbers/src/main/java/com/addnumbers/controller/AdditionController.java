package com.addnumbers.controller;

import com.addnumbers.service.AdditionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdditionController {

    @Autowired
    private AdditionService additionService;

    @ApiOperation(value ="Get the Client Message")
    @RequestMapping(method = RequestMethod.GET, value = "/clientMsg")
    public String getClientMsg() {
        return additionService.getClientMsg();
    }

    @ApiOperation(value= " Get the Service API")
    @RequestMapping(method = RequestMethod.POST, value = "/ServiceMsg")
    public ResponseEntity<String> getServiceMsg(HttpServletRequest request, HttpServletResponse response) {
        String msg = additionService.getServiceMsg();
        String msgs[] = msg.split("token:");
        response.addHeader("token",msgs[1]);
        return ResponseEntity.ok(msgs[0]);
    }


    @ApiOperation(value = "Get the Final Message")
    @RequestMapping(method = RequestMethod.POST, value = "/finalMessage")
    public ResponseEntity<String> finalMessage(@RequestParam("question") List<Integer> question,
                               @RequestParam(name = "total") int total,
                                               HttpServletRequest request) {
        String token = request.getHeader("token");
        return additionService.finalMessage(question, total, token);
    }

    public AdditionService getAdditionService() {
        return additionService;
    }

    public void setAdditionService(AdditionService additionService) {
        this.additionService = additionService;
    }
}