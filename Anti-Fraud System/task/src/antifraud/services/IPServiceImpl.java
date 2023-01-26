package antifraud.services;

import antifraud.database.IPs;
import antifraud.errors.IpDuplicateException;
import antifraud.models.DTO.IPResponse;
import antifraud.repositories.IPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IPServiceImpl implements IPService {

    private final IPRepository ipRepository;

    @Override
    @Transactional
    public IPResponse saveIp(IPs ip) throws IpDuplicateException {

        Optional<IPs> optionalIP = ipRepository.findIPsByIp(ip.getIp());
        if (optionalIP.isEmpty()) {
            ipRepository.save(ip);
        } else {
            throw new IpDuplicateException("IP already persists");
        }
        Long id = ipRepository.findById(ip.getId()).get().getId();
        return new IPResponse(id, ip.getIp());
    }
}
