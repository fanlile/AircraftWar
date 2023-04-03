package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Prop_BloodTest {
    private Prop_Blood prop_blood;
    private HeroAircraft heroAircraft;
    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
        prop_blood = new Prop_Blood(
                100,
                100,
                0,
                6,
                100
        );
        heroAircraft = HeroAircraft.getHeroAircraft();
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
        prop_blood = null;
    }

    @Test
    void notValid() {
        System.out.println("**--- Test notValid method executed ---**");
        assertFalse(prop_blood.notValid());
    }

    @Test
    void active() {
        System.out.println("**--- Test active method executed ---**");
        heroAircraft.decreaseHp(500);
        prop_blood.active(heroAircraft);
        assertEquals(2600,heroAircraft.getHp());
    }

}