package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Floor extends GameObject {

    public Floor(Position position) {
        super(position);
    }

    @Override
    protected boolean isPassable() {
        return true;
    }

    @Override
    public String getName() {
        return "Floor";
    }
}
