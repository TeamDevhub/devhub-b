package teamdevhub.devhub.adapter.out.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.adapter.out.common.entity.BaseEntity;
import teamdevhub.devhub.adapter.out.common.entity.BooleanToYNConverter;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_email",
                        columnNames = "userGuid"
                )
        }
)
public class UserEntity extends BaseEntity {

    @Id @Column(length = 32, nullable = false, unique = true)
    private String userGuid;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String username;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @Column(length = 500)
    private String introduction;

    @Column(nullable = false)
    private double mannerDegree;

    @Column(nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean blocked;

    private LocalDateTime blockEndDate;

    @Column(nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean deleted;

    private LocalDateTime lastLoginDt;
}