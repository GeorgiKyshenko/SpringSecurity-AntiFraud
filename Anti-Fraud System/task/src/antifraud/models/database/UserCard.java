package antifraud.models.database;

import antifraud.constants.AmountVerification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor@NoArgsConstructor
@Getter@Setter
@Entity
@Table(name = "user_cards", indexes = {@Index(name = "user_card_number", columnList = "number")})
public class UserCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String number;
    @Column(name = "allowed_value")
    private long allowedValue = AmountVerification.ALLOWED.getAmount();
    @Column(name = "manual_value")
    private long manualValue = AmountVerification.MANUAL_PROCESSING.getAmount();

    public UserCard(String number) {
        this.number = number;
        this.allowedValue = 200;
        this.manualValue = 1500;
    }
}
