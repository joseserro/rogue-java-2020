package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.*;
import pt.upskills.projeto.rogue.utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Engine {
    private static final Engine INSTANCE = new Engine();

    public static Engine getInstance() {
        return INSTANCE;
    }

    private Map<Integer, Room> rooms = new HashMap<>();
    private int currentRoom;

    public List<ImageTile> getCurrentRoomTiles() {
        return rooms.get(currentRoom).getTiles();
    }

    //init apenas composto pelo startGame (que pode ser usado para fazer restart)
    public void init(){
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

        startGame();

        gui.go();
        while (true){
            gui.update();
        }
    }

    //método chamado para passar para a próxima sala
    public void nextRoom(int room) {
        //atualizamos a variavel currentRoom
        currentRoom = room;

        //apagamos todas as imagens e observers da gui
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.clearImages();
        gui.deleteObservers();

        //adicionamos as novas imagens e observers
        gui.newImages(getCurrentRoomTiles());
        for(ImageTile tile : getCurrentRoomTiles()) {
            if(tile instanceof Movable) {
                gui.addObserver((Observer)tile);
            }
        }

        //adicionamos o hero no fim, para ficar por cima das tiles
        gui.addObserver(Hero.getInstance());
        gui.addImage(Hero.getInstance());
    }

    public void startGame() {
        //colocamos o hero = null para o startGame inicializar com um novo herói
        //ou seja, sem que o herói mantenha a vida, inventorio e bolas de fogo
        Hero.resetInstance();
        //ler o mapa, vai preencher recursivamente as salas
        readMapFile(0, null);
        //atualizamos a status bar para inicializar ou fazer reset
        updateStatus();
        //iniciamos o jogo na sala 0
        nextRoom(0);
    }

    //método que vai fazer refresh à status bar, redesenhar as fireballs, vida e inventorio
    public void updateStatus() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        Hero hero = Hero.getInstance();

        gui.clearStatus();
        List<ImageTile> statusTiles = new ArrayList<>();
        //geramos o background da statusbar com tiles pretas
        for(int i = 0; i<10; i++) {
            //iteramos de 0 a 9 para colocar uma em cada posição X da statusbar
            statusTiles.add(new BlackTile(new Position(i, 0)));
        }
        //fireballs gerada com base no número de fireballs que o hero tem (entre 0 e 3)
        for(int i = 0; i < hero.getFireballs(); i++) {
            statusTiles.add(new FireBall(new Position(i, 0)));
        }
        //healthbar (ver diagrama e explicação na pasta do projeto)
        int i = 0;
        for(; i < hero.getHp(); i+=2) {
            statusTiles.add(new GreenTile(new Position(i/2+3, 0)));
        }
        if (hero.getHp() % 2 != 0) {
            statusTiles.add(new RedGreenTile(new Position((i - 1) / 2 + 3, 0)));
        }
        for(; i < 8; i+=2) {
            statusTiles.add(new RedTile(new Position(i/2+3, 0)));
        }

        //inventory
        for(i = 0; i < 3; i++) {
            if(hero.getInventory().containsKey(i)) {
                Item item = hero.getInventory().get(i);
                item.setPosition(new Position(i + 7, 0));
                statusTiles.add(item);
            }
        }

        gui.newStatusImages(statusTiles);
    }

    //devolve a posição da porta do nivel anterior
    //só é necessário chamar a primeira vez com o "level = 0" para carregar
    //recursivamente os outros nivels através do mapa
    private Position readMapFile(int level, Door prevLevelDoor) {
        //implementação do método...
        //ler o ficheiro rooms/room + level + .txt

        //primeiro criamos um mapa com as portas desta sala
        Map<Integer, Door> portas = new HashMap<>();
        //depois a lista de telas completa da sala
        List<ImageTile> tiles = new ArrayList<>();
        //e inicializamos o currentDoorPosition como null para poder
        //depois atualizar quando já tivermos as portas
        Position currentDoorPosition = null;

        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                tiles.add(new Floor(new Position(i, j)));
            }
        }

        try {
            Scanner fileScanner = new Scanner(new File("rooms/room" + level + ".txt"));
            int i = 0;
            while(fileScanner.hasNextLine()) {
                String nextLine = fileScanner.nextLine();
                String[] separar = nextLine.split("");
                //se começar por #, ou seja, o "header" do ficheiro da sala
                if(separar[0].equals("#")) {
                    String[] sepRoom = nextLine.split(" ");
                    if(sepRoom.length > 1) { //se tiver informação
                        //o state representa se tem chave (ou se for uma stairwell, se vai para cima)
                        boolean state = sepRoom.length > 5;
                        int doorNumber = Integer.parseInt(sepRoom[1]);
                        int nextRoom = Integer.parseInt(sepRoom[3].split("\\.")[0].split("room")[1]);
                        //criamos a porta com a informação sobre o numero da porta, a sala para qua vai
                        //o tipo de porta, e o estado (aberta/fechada ou up/down)
                        Door door = new Door(doorNumber, nextRoom, Door.Type.valueOf(sepRoom[2]), state);
                        //guardamos as portas desta sala num para para poder depois adicionar a posição
                        //e saber qual é a porta que dá à sala atual
                        portas.put(doorNumber, door);
                        //e adicionamos às tiles
                        tiles.add(door);
                    }
                } else {
                    //por cada linha, iterar agora por cada caracter da linha
                    for (int j = 0; j < separar.length; j++) {
                        //posicao j, i (j o indice do caracter, i a linha)
                        Position pos = new Position(j, i);

                        //iterar as portas pela chave (numero da porta)
                        for(Integer key : portas.keySet()) {
                            Door door = portas.get(key);
                            //se o nosso numero for encontrado no mapa da sala
                            if(separar[j].equals(key.toString())) {
                                //sabemos qual é a proxima sala
                                int nextRoom = door.getNextRoom();
                                //sabemos também a posição dessa porta
                                door.setPosition(pos);
                                Position nextDoorPosition;

                                //se é a primeira sala e a chave é igual à porta da sala anterior
                                if(prevLevelDoor != null && key.equals(prevLevelDoor.getDoorNumber())) {
                                    //sabemos que a posição da "próxima" é a anterior
                                    nextDoorPosition = prevLevelDoor.getPosition();
                                    //e a atual é a que estamos agora a iterar
                                    currentDoorPosition = pos;
                                } else if(rooms.get(nextRoom) == null) {
                                    //se ainda não temos a proxima sala carregada para o "rooms"
                                    //chamamos recursivamente o readMapFile com a proxima sala
                                    nextDoorPosition = readMapFile(nextRoom, door);
                                } else {
                                    //caso já exista, por exemplo no caso em que uma sala dá acesso a outra
                                    //que já foi carregada (ex: 0 -> 1 -> 2 -> 3 -> 1)
                                    nextDoorPosition = rooms.get(nextRoom).getCurrentDoorPosition();
                                }

                                //sabendo agora a posição da porta seguinte, podemos fazer set na porta
                                //para que quando o hero passe de nível, vá parar à porta certa
                                if(nextDoorPosition == null) {
                                    nextDoorPosition = pos; //caso por acaso não conseguimos carregar a sala seguinte corretamente
                                }
                                door.setNextDoorPosition(nextDoorPosition);

                            }
                        }

                        //adicionar tiles dependendo do tipo de caracter encontrado
                        switch (separar[j]) {
                            //paredes
                            case "W":
                                tiles.add(new Wall(pos));
                                break;
                            //no caso do hero só atualizamos a posição
                            //ou criamos de novo caso exista (por exemplo num restart)
                            case "h":
                                Hero.getInstance().setPosition(pos);
                                break;
                            //esqueletos
                            case "S":
                                Skeleton skeleton = new Skeleton(pos);
                                tiles.add(skeleton);
                                break;
                            //fireball item, o "apanhável", não o que lançamos
                            case "f":
                                FireBallItem fireBall = new FireBallItem(pos);
                                tiles.add(fireBall);
                                break;
                            //a espada
                            case "s":
                                Sword sword = new Sword(pos);
                                tiles.add(sword);
                                break;
                            //chave
                            case "k":
                                Key key = new Key(pos);
                                tiles.add(key);
                                break;
                            //podemos adicionar aqui outros (mais itens, inimigos, paredes, traps)
                        }
                    }
                    i++;
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Não foi possível ler o ficheiro do nível " + level);
            return null;
        }

        //por fim criamos a sala com as tiles, portas e posição da porta atual
        //e adicionamos ao mapa de rooms do Engine
        Room room = new Room(tiles, portas, currentDoorPosition);
        rooms.put(level, room);
        return currentDoorPosition;
    }

    public static void main(String[] args){
        Engine.getInstance().init();
    }
}
