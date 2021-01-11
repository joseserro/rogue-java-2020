package pt.upskills.projeto.objects;

import pt.upskills.projeto.rogue.utils.Position;

public class RedTile extends GameObject {
    public RedTile(Position position) {
        super(position);
    }

    @Override
    protected boolean isPassable() {
        return false;
    }

    @Override
    public String getName() {
        return "Red";
    }
}
