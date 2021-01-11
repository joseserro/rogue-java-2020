package pt.upskills.projeto.objects;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.rogue.utils.Position;

public class Sword extends Weapon {
    public Sword(Position pos) {
        super(pos);
    }

    @Override
    public int getPower() {
        return 2;
    }

    @Override
    protected boolean isPassable() {
        return true;
    }

    @Override
    public void interact(Hero hero) {
        hero.pickUp(this);
    }

    @Override
    public String getName() {
        return "Sword";
    }
}
