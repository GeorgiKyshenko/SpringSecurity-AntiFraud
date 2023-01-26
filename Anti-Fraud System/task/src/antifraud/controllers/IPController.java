package antifraud.controllers;

import antifraud.database.IPs;
import antifraud.errors.IpDuplicateException;
import antifraud.models.DTO.IPResponse;
import antifraud.services.IPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class IPController {

    private final IPService ipService;

    @PostMapping("/api/antifraud/suspicious-ip")
    @PreAuthorize("hasRole('SUPPORT')")
    public ResponseEntity<IPResponse> saveIPAddress(@RequestBody @Valid IPs ip) throws IpDuplicateException {
        IPResponse ipResponse = ipService.saveIp(ip);
        return ResponseEntity.status(200).body(ipResponse);
    }

}
