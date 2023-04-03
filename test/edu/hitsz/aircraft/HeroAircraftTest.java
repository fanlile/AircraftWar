package edu.hitsz.aircraft;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {
    private HeroAircraft heroAircraft;
    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
        heroAircraft = HeroAircraft.getHeroAircraft();
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
        heroAircraft = null;
    }

    @Test
    void getHp() {
        System.out.println("**--- Test getHp method executed ---**");
        assertEquals(3000,heroAircraft.getHp());
    }

    @Test
    void decreaseHp() {
        System.out.println("**--- Test decreaseHp method executed ---**");
        heroAircraft.decreaseHp(100);
        assertEquals(2900,heroAircraft.getHp());
    }

    @Test
    void increaseHp() {
        System.out.println("**--- Test increaseHp method executed ---**");
        heroAircraft.decreaseHp(200);
        heroAircraft.increaseHp(300);
        assertEquals(3000,heroAircraft.getHp());
    }

}