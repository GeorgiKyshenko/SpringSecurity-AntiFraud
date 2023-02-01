package antifraud.services;

import antifraud.models.database.IPs;
import antifraud.errors.IncorrectIpInput;
import antifraud.errors.IpDuplicateException;
import antifraud.errors.IpNotFoundException;
import antifraud.models.DTO.DeleteIPResponse;
import antifraud.models.DTO.IPResponse;

import java.util.List;

public interface IPService {
    IPResponse saveIp(IPs ip) throws IpDuplicateException;

    DeleteIPResponse deleteIp(String ip) throws IpNotFoundException;

    List<IPResponse> findAllIPs();
}
