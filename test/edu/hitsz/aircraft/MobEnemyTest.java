package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MobEnemyTest {
    private MobEnemy mobEnemy;

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
        mobEnemy = new MobEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0,
                6,
                30
        );
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
        mobEnemy = null;
    }

    @Test
    void getLocationX() {
        System.out.println("**--- Test getLocationX method executed ---**");
        assertTrue(mobEnemy.getLocationX() >= 0 && mobEnemy.getLocationX() < Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth());
    }

    @Test
    void getLocationY() {
        System.out.println("**--- Test getLocationY method executed ---**");
        assertTrue(mobEnemy.getLocationY() >= 0 && mobEnemy.getLocationY() < Main.WINDOW_HEIGHT * 0.05);
    }
}