package antifraud.services;

import antifraud.database.IPs;
import antifraud.errors.IpDuplicateException;
import antifraud.models.DTO.IPResponse;

public interface IPService {
    IPResponse saveIp(IPs ip) throws IpDuplicateException;
}
