package com.back2basics.lomoktest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberWithoutDataRepository extends JpaRepository<MemberWithoutData, Long> {

}
