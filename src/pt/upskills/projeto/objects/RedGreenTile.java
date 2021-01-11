package pt.upskills.projeto.objects;

import pt.upskills.projeto.rogue.utils.Position;

public class RedGreenTile extends GameObject {
    public RedGreenTile(Position position) {
        super(position);
    }

    @Override
    protected boolean isPassable() {
        return false;
    }

    @Override
    public String getName() {
        return "RedGreen";
    }
}
