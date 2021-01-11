package pt.upskills.projeto.commands;

import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.function.Consumer;

public abstract class HeroAction {
    public enum Action {
        Right ((hero -> {
            Position newPosition = hero.getPosition().plus(Direction.RIGHT.asVector());
            hero.setPosition(newPosition);
        })),
        Left ((hero -> {
            Position newPosition = hero.getPosition().plus(Direction.LEFT.asVector());
            hero.setPosition(newPosition);
        })),
        Space (hero -> {
            System.out.println("O user clicou no espaço");
        }),
        H (hero -> {
            System.out.println("Sou o herói!");
        });
        private Consumer<Hero> action;
        public void useAction(Hero hero) {
            action.accept(hero);
        }
        Action(Consumer<Hero> action) {
            this.action = action;
        }
    }
}
