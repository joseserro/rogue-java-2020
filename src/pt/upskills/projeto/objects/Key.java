package pt.upskills.projeto.objects;

import pt.upskills.projeto.rogue.utils.Position;

public class Key extends Item {
    public Key(Position position) {
        super(position);
    }

    @Override
    public void interact(Hero hero) {
        hero.pickUp(this);
    }

    @Override
    protected boolean isPassable() {
        return true;
    }

    @Override
    public String getName() {
        return "Key";
    }
}
