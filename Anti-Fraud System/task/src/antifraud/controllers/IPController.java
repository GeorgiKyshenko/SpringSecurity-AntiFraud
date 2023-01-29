package antifraud.controllers;

import antifraud.models.database.IPs;
import antifraud.errors.IncorrectIpInput;
import antifraud.errors.IpDuplicateException;
import antifraud.errors.IpNotFoundException;
import antifraud.models.DTO.DeleteIPResponse;
import antifraud.models.DTO.IPResponse;
import antifraud.services.IPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/antifraud/")
@PreAuthorize("hasRole('SUPPORT')")
public class IPController {

    private final IPService ipService;

    @PostMapping("/suspicious-ip")
    public ResponseEntity<IPResponse> saveIPAddress(@RequestBody @Valid IPs ip) throws IpDuplicateException {
        IPResponse ipResponse = ipService.saveIp(ip);
        return ResponseEntity.status(200).body(ipResponse);
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<DeleteIPResponse> deleteIp(@PathVariable String ip) throws IpNotFoundException, IncorrectIpInput {
        ipService.validateString(ip);
        DeleteIPResponse deleteIP = ipService.deleteIp(ip);
        return ResponseEntity.status(200).body(deleteIP);
    }

    @GetMapping("/suspicious-ip")
    public List<IPResponse> getAllIPs() {
        return ipService.findAllIPs();
    }
}
