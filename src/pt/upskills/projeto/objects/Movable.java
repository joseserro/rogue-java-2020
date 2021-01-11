package pt.upskills.projeto.objects;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.Observer;

/**
 * Classe Movable extendida pelo Hero e pelos inimigos
 * Utilizada para objetos do jogo subscritos como Observers ao update da gui
 */
public abstract class Movable extends GameObject implements Observer {
    public Movable(Position position) {
        super(position);
    }

    //método canMove, utilizado pelo hero e inimigos para verificar se podem
    //avançar para uma determinada posição. Se for encontrado um objeto que tem
    //o isPassable a false, retorna false, ou seja, não pode avançar
    //caso chegue ao fim do ciclo sem encontrar nenhum objeto "não passável", retorna true
    public boolean canMove(Position nextPosition) {
        for(ImageTile tile : Engine.getInstance().getCurrentRoomTiles()) {
            if(tile instanceof GameObject) {
                GameObject gameObject = (GameObject) tile;
                if(tile.getPosition().equals(nextPosition) && !gameObject.isPassable()) {
                    return false;
                }
            }
        }
        return true;
    }
}
