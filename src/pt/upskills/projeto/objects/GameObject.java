package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

/**
 * Classe GameObject abstrata que todos os objetos do jogo devem extender
 * Inclui já a Position + construtor/getter/setter da Position
 * É necessário implementar o isPassable ao extender
 */
public abstract class GameObject implements ImageTile {
    private Position position;

    public GameObject(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    //isPassable vai ser um método que retorna apenas true or false,
    //dependendo do tipo de objeto (uma parede não é passable, floor é passable)
    //As classes movable vão verificar através do isPassable se podem passar ou não
    //ver método canMove na classe Movable
    protected abstract boolean isPassable();
}
