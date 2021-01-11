package pt.upskills.projeto.objects;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.Random;

public class Skeleton extends Enemy {
    public Skeleton(Position position) {
        super(position, EnemyType.SKELETON);
    }

    @Override
    public Position nextEnemyPosition() {
        Random rand = new Random();
        double distance = getDistanceToHero();
        Position nextPosition = getPosition();
        if(distance < 8) {
            //seguir o jogador
            Position heroPosition = Hero.getInstance().getPosition();
            int diffx = heroPosition.getX() - getPosition().getX();
            int diffy = heroPosition.getY() - getPosition().getY();
            if(rand.nextBoolean() && diffx != 0) {
                if(diffx > 0) {
                    nextPosition = getPosition().plus(Direction.RIGHT.asVector());
                } else {
                    nextPosition = getPosition().plus(Direction.LEFT.asVector());
                }
            } else {
                if(diffy > 0) {
                    nextPosition = getPosition().plus(Direction.DOWN.asVector());
                } else if(diffy < 0) {
                    nextPosition = getPosition().plus(Direction.UP.asVector());
                }
            }
        } else {
            //andar aleatoriamente
            int randomInt = rand.nextInt(4);
            nextPosition = getPosition().plus(
                    Direction.values()[randomInt].asVector());
        }
        return nextPosition;
    }

    @Override
    public String getName() {
        return "Skeleton";
    }
}
