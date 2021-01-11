package pt.upskills.projeto.objects;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.Observable;

public abstract class Enemy extends Movable {

    public void damage(int dano) {
        this.hp -= dano;
        if(this.hp <= 0) {
            Engine.getInstance().getCurrentRoomTiles().remove(this);
            ImageMatrixGUI.getInstance().removeImage(this);
            ImageMatrixGUI.getInstance().deleteObserver(this);
        }
    }

    public enum EnemyType {
        SKELETON(5,1),
        BAT(2,2),
        THIEF(6, 2);

        private final int hp;
        private final int dano;
        EnemyType(int hp, int dano) {
            this.hp = hp;
            this.dano = dano;
        }

        public int getHp() {
            return hp;
        }

        public int getDano() {
            return dano;
        }
    }

    private int hp;
    private int dano;

    public Enemy(Position position, EnemyType enemyType) {
        super(position);
        this.hp = enemyType.getHp();
        this.dano = enemyType.getDano();
    }

    public int getHp() {
        return hp;
    }

    public int getDano() {
        return dano;
    }

    public abstract Position nextEnemyPosition();

    double getDistanceToHero() {
        Position heroPosition = Hero.getInstance().getPosition();
        return Math.sqrt(
                Math.pow(heroPosition.getX() - getPosition().getX(), 2) +
                Math.pow(heroPosition.getY() - getPosition().getY(), 2)
                );
    }

    @Override
    protected boolean isPassable() {
        return false;
    }

    private boolean isHeroAt(Position position) {
        return Hero.getInstance().getPosition().equals(position);
    }

    private int recursiveMoveCount;

    @Override
    public void update(Observable o, Object arg) {
        Position nextPosition = nextEnemyPosition();
        if(nextPosition.equals(getPosition())) {
            return;
        }
        if(isHeroAt(nextPosition)) {
            Hero.getInstance().takeDamage(getDano());
        } else if(canMove(nextPosition)) {
            setPosition(nextPosition);
        } else if(recursiveMoveCount < 5) {
            recursiveMoveCount++;
            update(o,arg);
            return;
        }
        recursiveMoveCount = 0;
    }
}
