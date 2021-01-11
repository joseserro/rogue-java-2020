package pt.upskills.projeto.objects;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.FireTile;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class FireBall extends GameObject implements FireTile {
    public FireBall(Position position) {
        super(position);
    }

    @Override
    public boolean validateImpact() {
        //return false quando tocar nalgum objeto
        for(ImageTile tile : Engine.getInstance().getCurrentRoomTiles()) {
            if(tile.getPosition().equals(getPosition())){
                //se esse objeto for um inimigo, dar dano
                if(tile instanceof Enemy) {
                    ((Enemy) tile).damage(2);
                }
                //caso não seja "passable", apagar a bola de fogo e retornar false
                if(tile instanceof GameObject && !(tile instanceof FireBall) && !((GameObject) tile).isPassable()) {
                    Engine.getInstance().getCurrentRoomTiles().remove(this);
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected boolean isPassable() {
        return false;
    }

    @Override
    public String getName() {
        return "Fire";
    }
}
