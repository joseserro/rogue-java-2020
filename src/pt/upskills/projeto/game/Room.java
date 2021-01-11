package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Door;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.List;
import java.util.Map;

public class Room {
    private List<ImageTile> tiles;
    private Map<Integer, Door> portas;
    private Position currentDoorPosition;

    public Room(List<ImageTile> tiles, Map<Integer, Door> portas, Position currentDoorPosition) {
        this.tiles = tiles;
        this.portas = portas;
        this.currentDoorPosition = currentDoorPosition;
    }

    public List<ImageTile> getTiles() {
        return tiles;
    }

    public Map<Integer, Door> getPortas() {
        return portas;
    }

    public Position getCurrentDoorPosition() {
        return currentDoorPosition;
    }
}
