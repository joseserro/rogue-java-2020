package pt.upskills.projeto.objects;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.rogue.utils.Position;

public class FireBallItem extends Item {
    public FireBallItem(Position position) {
        super(position);
    }

    @Override
    public void interact(Hero hero) {
        if(hero.getFireballs() < 3) {
            hero.addFireball();
            Engine.getInstance().getCurrentRoomTiles().remove(this);
            ImageMatrixGUI.getInstance().removeImage(this);
        }
    }

    @Override
    protected boolean isPassable() {
        return true;
    }

    @Override
    public String getName() {
        return "Fire";
    }
}
