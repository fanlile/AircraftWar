package edu.hitsz.factory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.Enemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.Strategy.ScatterShoot;

public class BossEnemyFactory implements EnemyFactory {
    @Override
    public Enemy createEnemy(){
        Enemy bossEnemy = new BossEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                115,
                5,
                0,
                400);
        bossEnemy.setShootStrategy(new ScatterShoot());
        return bossEnemy;
    }
}
