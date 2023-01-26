package antifraud.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
@Entity
@Table(name = "ip_addresses")
public class IPs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(nullable = false)
    private Long id;

    @Column(name = "suspicious_ip", nullable = false)
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$", message = "Not valid IP") // IPv4 regex pattern
    @NotNull
    private String ip;
}
