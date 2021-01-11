package pt.upskills.projeto.objects;

import pt.upskills.projeto.rogue.utils.Position;

public abstract class Weapon extends Item {
    public Weapon(Position position) {
        super(position);
    }

    public abstract int getPower();
}
