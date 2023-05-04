package edu.hitsz.factory;

import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.Enemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Card.Main;
import edu.hitsz.strategy.DirectShoot;

public class EliteEnemyFactory implements EnemyFactory {
    @Override
    public Enemy createEnemy(){
        Enemy eliteEnemy = new EliteEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0,
                5,
                120);
        eliteEnemy.setShootStrategy(new DirectShoot());
        return eliteEnemy;
    }
}
