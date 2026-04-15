package com.school.teacherarchivesystem.repository;

import com.school.teacherarchivesystem.entity.SystemUser;
import com.school.teacherarchivesystem.enums.UserRole;
import com.school.teacherarchivesystem.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    Optional<SystemUser> findByUsername(String username);
    List<SystemUser> findByRoleOrderByIdAsc(UserRole role);
    List<SystemUser> findByRoleAndStatusOrderByIdAsc(UserRole role, UserStatus status);
    boolean existsByUsername(String username);
    boolean existsByTeacherNo(String teacherNo);
}
