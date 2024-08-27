package com.jayarajsrivathsav.management_service.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import com.jayarajsrivathsav.management_service.model.Service;
import com.jayarajsrivathsav.management_service.repository.serviceRepo;

@RestController
@RequestMapping("/api/services")
public class serviceController {

    @Autowired
    private serviceRepo repo;

    @PostMapping("/save")
    public Service createService(@RequestBody Service service) {
        return repo.save(service);
    }

    @GetMapping("/fetch")
    public List<Service> getAllServices() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable String id) {
        Optional<Service> service = repo.findById(id);
        if(service.isPresent()) {
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable String id, @RequestBody Service serviceDetails) {
        Optional<Service> serviceData = repo.findById(id);

        if(serviceData.isPresent()) {
            Service updatedService = serviceData.get();
            updatedService.setNodeName(serviceDetails.getNodeName());
            updatedService.setDescription(serviceDetails.getDescription());
            updatedService.setMemo(serviceDetails.getMemo());
            updatedService.setNodeType(serviceDetails.getNodeType());
            updatedService.setParentNodeGroupName(serviceDetails.getParentNodeGroupName());
            updatedService.setParentNodeGroupId(serviceDetails.getParentNodeGroupId());
            updatedService.setParentNodeGroupType(serviceDetails.getParentNodeGroupType());

            repo.save(updatedService);
            return ResponseEntity.ok(updatedService);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable String id) {
        try {
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/bulk-import")
    public ResponseEntity<?> importServicesFromCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<Service> services = new ArrayList<>();
            String line;
            boolean headerSkipped = false;

            while ((line = fileReader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; 
                
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length != 8) {
                    continue; 
                }

                
                Service service = new Service(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
                services.add(service);
            }

            
            if (!services.isEmpty()) {
                repo.saveAll(services);
                return ResponseEntity.ok("Uploaded and saved " + services.size() + " services");
            } else {
                return ResponseEntity.badRequest().body("No valid data found to import.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Service>> searchServices(@RequestParam String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        List<Service> foundServices = serviceRepo.findByKeyword(keyword);
        if (foundServices.isEmpty()) {
            return ResponseEntity.noContent().build();  
        }
        return ResponseEntity.ok(foundServices);
    }

}
