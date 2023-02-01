package antifraud.services;

import antifraud.models.database.IPs;
import antifraud.errors.IncorrectIpInput;
import antifraud.errors.IpDuplicateException;
import antifraud.errors.IpNotFoundException;
import antifraud.models.DTO.DeleteIPResponse;
import antifraud.models.DTO.IPResponse;
import antifraud.repositories.IPRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IPServiceImpl implements IPService {

    private final IPRepository ipRepository;
    private final ModelMapper mapper;

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

    @Override
    @Transactional
    public DeleteIPResponse deleteIp(String ip) throws IpNotFoundException {
        Optional<IPs> optionalIP = ipRepository.findIPsByIp(ip);
        if (optionalIP.isEmpty()) throw new IpNotFoundException("IP not found");
        ipRepository.deleteByIp(ip);
        return new DeleteIPResponse(String.format("IP %s successfully removed!", ip));
    }

    @Override
    public List<IPResponse> findAllIPs() {
        return ipRepository.findAll(Sort.sort(IPs.class).by(IPs::getId).ascending())
                .stream()
                .map(ip -> mapper.map(ip, IPResponse.class)).toList();
    }
}
