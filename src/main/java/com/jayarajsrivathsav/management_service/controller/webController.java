package com.jayarajsrivathsav.management_service.controller;

import java.io.PrintWriter;
import java.util.List;


import jakarta.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jayarajsrivathsav.management_service.model.Service;
import com.jayarajsrivathsav.management_service.repository.serviceRepo;

@Controller
public class webController {

    @Autowired
    private serviceRepo repo;

    @GetMapping("/web")
    public String showServices(Model model) {
        model.addAttribute("nodes", repo.findAll());
        return "services"; 
    }

    @GetMapping("/export-csv")
    public void exportToCSV(HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"services.csv\"");

        List<Service> services = repo.findAll();
        PrintWriter writer = response.getWriter();

        writer.println("Node ID,Node Name,Description,Memo,Node Type,Parent Node Group Name,Parent Node Group ID,Parent Node Group Type");
        for (Service service : services) {
            writer.println(String.join(",",
                service.getNodeId(),
                service.getNodeName(),
                service.getDescription(),
                service.getMemo(),
                service.getNodeType(),
                service.getParentNodeGroupName(),
                service.getParentNodeGroupId(),
                service.getParentNodeGroupType()));
        }
        writer.flush();
    }
}
