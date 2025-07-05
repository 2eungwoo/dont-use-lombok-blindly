package com.back2basics.lomoktest;


import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LombokTest {

    private static final int COUNT = 100000;

    @Autowired
    private MemberWithoutDataRepository personOnlyNeededRepository;

    @Autowired
    private MemberWithDataRepository personWithDataRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        for (int i = 0; i < COUNT; i++) {
            personWithDataRepository.save(new MemberWithData("김", "20"));
            personOnlyNeededRepository.save(new MemberWithoutData("이", "21"));
        }
    }

    @AfterEach
    @Transactional
    public void clearDatabase() {
        personOnlyNeededRepository.deleteAll();
        personWithDataRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName(" save 성능 테스트 ")
    public void testSavePerformance() {
        long dataStart = System.nanoTime();
        for (int i = 0; i < COUNT; i++) {
            personWithDataRepository.save(new MemberWithData("김", "22"));
        }
        long dataEnd = System.nanoTime();
        long dataTime = dataEnd - dataStart;

        long manualStart = System.nanoTime();
        for (int i = 0; i < COUNT; i++) {
            personOnlyNeededRepository.save(new MemberWithoutData("이", "23"));
        }
        long manualEnd = System.nanoTime();
        long manualTime = manualEnd - manualStart;

        System.out.println("@Data 저장 시간(ns): " + dataTime);
        System.out.println("필요한 어노테이션만 저장 시간(ns): " + manualTime);

        assertThat(dataTime).isGreaterThan(manualTime);
    }

    @Test
    @Transactional
    @DisplayName(" read 성능 테스트 ")
    public void testFindPerformance() {
        long dataStart = System.nanoTime();
        for (long i = 1; i <= COUNT; i++) {
            personWithDataRepository.findById(i);
        }
        long dataEnd = System.nanoTime();
        long dataFindTime = dataEnd - dataStart;

        long manualStart = System.nanoTime();
        for (long i = 1; i <= COUNT; i++) {
            personOnlyNeededRepository.findById(i);
        }
        long manualEnd = System.nanoTime();
        long manualFindTime = manualEnd - manualStart;

        System.out.println("@Data 조회 시간(ns): " + dataFindTime);
        System.out.println("필요한 어노테이션만 조회 시간(ns): " + manualFindTime);

        assertThat(dataFindTime).isGreaterThan(manualFindTime);
    }
}