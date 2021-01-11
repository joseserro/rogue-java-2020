package pt.upskills.projeto.objects;

import pt.upskills.projeto.rogue.utils.Position;

public class Door extends GameObject {

    public enum Type {
        D("Door", new String[]{"Closed", "Open"}),
        E("DoorWay", new String[]{"", ""}),
        S("Stairs", new String[]{"Up", "Down"});

        String doorType;
        String[] possibleStates;
        Type(String doorType, String[] possibleStates) {
            this.doorType = doorType;
            this.possibleStates = possibleStates;
        }

        public String getDoorType(boolean state) {
            return this.doorType + this.possibleStates[state ? 0 : 1];
        }
    }

    private boolean state;
    private int doorNumber;
    private int nextRoom;
    private Type type;
    private Position nextDoorPosition;

    public Door(int doorNumber, int nextRoom, Type type, boolean state) {
        super(null);
        this.doorNumber = doorNumber;
        this.nextRoom = nextRoom;
        this.type = type;
        this.state = state;

        //throw new IllegalArgumentException();
    }

    public int getNextRoom() {
        return nextRoom;
    }

    public int getDoorNumber() {
        return doorNumber;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    protected boolean isPassable() {
        return type.equals(Type.D) && !state;
    }

    public void openDoor() {
        this.state = false;
    }

    @Override
    public String getName() {
        return type.getDoorType(state);
    }

    public void setNextDoorPosition(Position nextDoorPosition) {
        this.nextDoorPosition = nextDoorPosition;
    }

    public Position getNextDoorPosition() {
        return nextDoorPosition;
    }
}
