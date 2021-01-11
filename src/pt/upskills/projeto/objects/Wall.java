package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Wall extends GameObject {

    public Wall(Position position) {
        super(position);
    }

    @Override
    protected boolean isPassable() {
        return false;
    }

    @Override
    public String getName() {
        return "Wall";
    }
}
