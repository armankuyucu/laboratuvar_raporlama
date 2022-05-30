package com.example.laboratuvar_raporlama.controllers;

import com.example.laboratuvar_raporlama.domain.Laborant;
import com.example.laboratuvar_raporlama.domain.Report;
import com.example.laboratuvar_raporlama.repositories.LaborantRepository;
import com.example.laboratuvar_raporlama.repositories.ReportRepository;
import com.example.laboratuvar_raporlama.services.ReportService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
public class ReportController {
    private final ReportRepository reportRepository;
    private final LaborantRepository laborantRepository;
    private final ReportService reportService;
    public ReportController(ReportRepository reportRepository, LaborantRepository laborantRepository, ReportService reportService){
        this.reportRepository = reportRepository;
        this.laborantRepository = laborantRepository;
        this.reportService = reportService;
    }
    @GetMapping("add-report")
    public String addReportGetController(Model model){
        model.addAttribute("reports", reportRepository.findAll());
        model.addAttribute("report",new Report());
        return "add-report";
    }

    @PostMapping("add-report")
    public String addReportPostController(Report report, @RequestParam("file") MultipartFile file) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Laborant laborant = laborantRepository.findById(username);
        report.assignLaborant(laborant);
        reportService.saveReportToDB(file, report);
//        reportRepository.save(report);
//        System.out.println("fileNumber: " + report.getFileNumber() +
//                            " patientName: " + report.getPatientName() +
//                            " patientSurname: " + report.getPatientSurname() +
//                            " trIdentityNumber: " + report.getTrIdentityNumber() +
//                            " title: " + report.getTitle() +
//                            " details: " + report.getDetails() +
//                            " title: " + report.getTitle() +
//                            " date: " + report.getDate() +
//                            " picture: " + report.getPicture());
        return "redirect:/add-report";
    }

    @GetMapping("/picture/")
    public String getPicture(@RequestParam("picture_id") Long picture_id, Model model){
        Optional<Report> report = reportRepository.findById(picture_id);
        if(report.isPresent())
            model.addAttribute("report", report.get());
        else
            System.out.println("EMPTY!!!!!!!!!!!");
        return "picture";
    }
//    @RequestMapping(value = "/image/{image_id}", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<byte[]> getImage(@PathVariable("image_id") Long imageId) throws IOException {
//
//        byte[] imageContent = reportRepository.getPictureByFileNumber(imageId).getPicture();//get image from DAO based on id
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
//    }

}