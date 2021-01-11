package pt.upskills.projeto.commands;

import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;

public class MoveRight extends HeroAction {

    public static void action(Hero hero) {
        Position newPosition = hero.getPosition().plus(Direction.RIGHT.asVector());
        hero.setPosition(newPosition);
    }
}
