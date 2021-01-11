package pt.upskills.projeto.objects;

import pt.upskills.projeto.commands.HeroAction;
import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.game.FireBallThread;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.util.*;

public class Hero extends Movable {

    private static final Hero INSTANCE = new Hero(new Position(0,0));
    private static final int DANO_BASE = 1;

    private int hp;
    private int fireballs;
    private Map<Integer, Item> inventory = new HashMap<>();

    public static Hero getInstance() {
        return INSTANCE;
    }

    public static void resetInstance() {
        INSTANCE.hp = 8;
        INSTANCE.fireballs = 1;
        INSTANCE.inventory.clear();
    }

    public Hero(Position position) {
        super(position);
        this.hp = 8;
        this.fireballs = 1;
    }

    public int getHp() {
        return this.hp;
    }

    public int getFireballs() {
        return fireballs;
    }

    public Map<Integer, Item> getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return "Hero";
    }

    @Override
    protected boolean isPassable() {
        return false;
    }

    //método takeDamage chamado pelos inimigos quando encontram o hero
    public void takeDamage(int damage) {
        //removemos do hp do hero a damage dada pelo inimigo
        this.hp -= damage;
        //caso fique menor ou igual a 0
        if(this.hp <= 0) {
            //hero morreu!
            //recomeçamos o jogo. é possível adicionar aqui um ecrã de gameover
            //ou mostrar a leaderboard, etc
            Engine.getInstance().startGame();
        }
        Engine.getInstance().updateStatus();
    }

    //calcular o dano que o hero dá aos inimigos
    public int calcularDano() {
        //começamos com o dano_base (por defeito a 1)
        int dano = DANO_BASE;
        //verificamos o inventorio do hero por armas
        for(Item item : inventory.values()) {
            if(item instanceof Weapon) {
                //caso sejam encontradas armas, adicionar o power da arma ao dano
                dano += ((Weapon) item).getPower();
            }
        }
        return dano;
    }

    /**
     * findItem, findEnemy e findDoor são métodos idênticos
     * daí ter sido criado o genérico "find", ver comentários abaixo.
     * A lógica neste método é que vamos iterar a lista de tiles da sala atual
     * e de seguida verificar se são de um certo tipo (instanceof Item, instanceof Enemy, etc)
     * e se a posição é igual à nextPosition, e devolver essa tile convertida no Item, Enemy
     * Desta forma podemos simplesmente "perguntar" se existe um item, enemy, door, etc
     * na próxima posição que queremos que o herói vá, e devolve esse objeto caso encontre
     * e null caso não encontre.
     */

    private Item findItem(Position nextPosition) {
        for(ImageTile tile : Engine.getInstance().getCurrentRoomTiles()) {
            if(tile instanceof Item && tile.getPosition().equals(nextPosition)) {
                return (Item) tile;
            }
        }
        return null;
    }

    private Enemy findEnemy(Position nextPosition) {
        for(ImageTile tile : Engine.getInstance().getCurrentRoomTiles()) {
            if(tile instanceof Enemy && tile.getPosition().equals(nextPosition)) {
                return (Enemy) tile;
            }
        }
        return null;
    }

    private Door findDoor(Position nextPosition) {
        for(ImageTile tile : Engine.getInstance().getCurrentRoomTiles()) {
            if(tile instanceof Door && tile.getPosition().equals(nextPosition)) {
                return (Door) tile;
            }
        }
        return null;
    }

    /**
     * Método criado para conseguir generficiar o findItem, findEnemy, findDoor, etc
     * O objetivo, se seguirem a lógica implementada nos outros "find", é a mesma:
     * Iterar as telas da sala atual, se for da classe que pedimos e tiver na
     * posição nextPosition, devolve essa tile, com um cast para a classe tClass
     * Pode ser usado da seguinte forma:
     * Door door = find(Door.class, nextPosition);
     * Item item = find(Item.class, nextPosition);
     * Enemy enemy = find(Enemy.class, nextPosition);
     * etc
     * @param tClass - Objeto que representa o nome da classe a pedir
     * @param nextPosition - Posição a verificar
     * @param <T> - genérico, usamos o Class<T> para representar que vai ser essa a classe
     *           a ser usada (ver slides de genericidade)
     * @return uma instancia da tela encontrada, convertida na classe pedida
     */
    private <T> T find(Class<T> tClass, Position nextPosition) {
        for(ImageTile tile : Engine.getInstance().getCurrentRoomTiles()) {
            if(tClass.isInstance(tile) && tile.getPosition().equals(nextPosition)) {
                return tClass.cast(tile);
            }
        }
        return null;
    }

    /**
     * addFireball incrementa apenas o contador de fireballs
     * e redesenha o status
     */
    public void addFireball() {
        fireballs++;
        Engine.getInstance().updateStatus();
    }

    public int numInventory() {
        return inventory.size();
    }

    /**
     * addItemToInventory adiciona o item enviado no argumento.
     * É verificado nas posições possíveis (0, 1, 2) do inventório
     * se já existe algum item lá dentro, e adiciona no primeiro disponível
     * No fim é chamado o updateStatus para redesenhar o inventário da status bar
     */
    public void addItemToInventory(Item item) {
        for(int i = 0; i < 3; i++) {
            if(!inventory.containsKey(i)) {
                inventory.put(i, item);
                break;
            }
        }
        Engine.getInstance().updateStatus();
    }

    /**
     * Na função dropItem passamos um índice do inventório do item a remover
     * ou seja 0, 1 ou 2. Este vai ser chamado pelo update com as key presses
     */
    public void dropItem(int index) {
        //verificamos se o inventório efectivamente contém algum item nesse índice
        if(inventory.containsKey(index)) {
            //removemos com o .remove do mapa, é-nos devolvido o item
            Item item = inventory.remove(index);
            //colocamos a posição do hero na posição do item
            item.setPosition(getPosition());
            //adicionamos às tiles do room atual e à gui
            Engine.getInstance().getCurrentRoomTiles().add(item);
            ImageMatrixGUI.getInstance().addImage(item);
            //atualizamos o status para aparecer removido da status bar
            Engine.getInstance().updateStatus();
        }
    }

    /**
     * This method is called whenever the observed object is changed. This function is called when an
     * interaction with the graphic component occurs {{@link ImageMatrixGUI}}
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        Integer keyCode = (Integer) arg;

        /*
        try {
            String actionCode = KeyEvent.getKeyText(keyCode);
            HeroAction.Action.valueOf(actionCode).useAction(this);
        } catch (IllegalArgumentException e) {
        }
        */

        try {
            //aqui usamos o valueOf dos enumerados que nos permite ir buscar um Direction
            //com base numa string (ex. "DOWN" -> Direction.DOWN)
            //o KeyEvent.getKeyText(keyCode) devolve o nome da tecla pressionada
            //encapsulamos esta parte do código num try catch porque ao clicar numa tecla
            //que não seja de direção, não vai ser encontrada o valueOf dessa direção e
            //faz throw de uma excepção IllegalArgumentException
            Direction lastDir = Direction.valueOf(KeyEvent.getKeyText(keyCode).toUpperCase());

            //a próxima posição que queremos ir é a atual + a direção em vetor
            Position novaPosicao = getPosition().plus(lastDir.asVector());

            //verificar se a próxima posição for "out of bounds" do mapa
            if(novaPosicao.getX() < 0 || novaPosicao.getX() > 9
            || novaPosicao.getY() < 0 || novaPosicao.getY() > 9) {
                return;
            }

            //aqui verificamos se existe um item, um enemy, uma door na posição seguinte
            //tomamos proveito dos métodos criados anteriormente, podendo ser substituido
            //o findItem(novaPosicao) por find(Item.class, novaPosicao)
            Item item = findItem(novaPosicao);
            if(item != null) {
                //caso seja encontrado um item nessa nova posição, interagir com o item
                item.interact(this);
            }
            Enemy enemy = findEnemy(novaPosicao);
            if(enemy != null) {
                //caso seja encontrado um enemy, atacar o enemy com o cálculo do dano
                enemy.damage(calcularDano());
            }
            Door door = find(Door.class, novaPosicao);
            if(door != null) {
                //caso seja encontrada uma porta, vamos verificar primeiro se tem chave
                //e guardar o indice da chave no inventorio na variavel hasKey
                int hasKey = -1;
                for(int i = 0; i < 3; i++) {
                    //iteramos pelos índices 0, 1 e 2 e verfificamos se existe lá algum item
                    //e se esse item é do tipo Key
                    if(inventory.get(i) != null && inventory.get(i) instanceof Key) {
                        //encontramos a chave no indice i, colocamos no hasKey
                        hasKey = i;
                        //e terminamos o ciclo for (é utilizada sempre a primeira chave)
                        break;
                    }
                }
                //verificamos se a porta está aberta (usamos o isPassable para verificar)
                if(door.isPassable()) {
                    //caso esteja aberta, passamos para a proxima sala
                    //apagamos o hero da sala atual
                    Engine.getInstance().getCurrentRoomTiles().remove(this);
                    //mudamos a posição do hero para a posição da porta na próxima sala
                    setPosition(door.getNextDoorPosition());
                    //e chamamos o nextRoom que vai tratar de tudo o resto
                    Engine.getInstance().nextRoom(door.getNextRoom());
                } else if (hasKey >= 0) {
                    //caso esteja trancada e tenha chave abrimos a porta
                    door.openDoor();
                    //removemos a chave do inventório
                    inventory.remove(hasKey);
                    //e atualizamos a status bar para remover a chave
                    Engine.getInstance().updateStatus();
                }
                //terminamos a execução do update porque passamos para outra sala
                return;
            }
            if(canMove(novaPosicao)) {
                setPosition(novaPosicao);
            }
        } catch (IllegalArgumentException e) {
            //caso tenha sido clicada uma tecla que não seja Up, Down, Left, Right

            //código para determinar a direção da fireball a ser lançada
            Direction fireballDirection = null;
            switch(keyCode) {
                case KeyEvent.VK_W:
                    fireballDirection = Direction.UP;
                    break;
                case KeyEvent.VK_D:
                    fireballDirection = Direction.RIGHT;
                    break;
                case KeyEvent.VK_S:
                    fireballDirection = Direction.DOWN;
                    break;
                case KeyEvent.VK_A:
                    fireballDirection = Direction.LEFT;
                    break;
                case KeyEvent.VK_SPACE:
                    fireballDirection = Direction.random();
                    break;

                //usar as teclas 1, 2 e 3 para fazer drop dos items
                case KeyEvent.VK_1:
                case KeyEvent.VK_NUMPAD1:
                    dropItem(0);
                    break;
                case KeyEvent.VK_2:
                case KeyEvent.VK_NUMPAD2:
                    dropItem(1);
                    break;
                case KeyEvent.VK_3:
                case KeyEvent.VK_NUMPAD3:
                    dropItem(2);
                    break;

                //usar a tecla R para fazer restart ao jogo
                case KeyEvent.VK_R:
                    //inicializamos o jogo de novo
                    Engine.getInstance().startGame();
                    return;
            }

            //se a fireballDirection não for null e tivermos mais do que 0 fireballs
            //o fireballDirection só é preenchido ao clicar no WASD ou espaço (no switch case)
            if(fireballDirection != null && fireballs > 0) {
                FireBall fireBall = new FireBall(getPosition());
                FireBallThread fireBallThread =
                        new FireBallThread(fireballDirection, fireBall);
                fireBallThread.start();

                Engine.getInstance().getCurrentRoomTiles().add(fireBall);
                ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
                gui.addImage(fireBall);
                fireballs--;
                Engine.getInstance().updateStatus();
            }
        }

    }

    //método pickup chamado como "callback" pelo próprio item no método interact
    //ver interact no Sword ou Key
    public void pickUp(Item item) {
        //caso tenhamos espaço para apanhar o item
        if(numInventory() < 3) {
            //adicionamos ao inventorio
            addItemToInventory(item);
            //removemos das telas da sala atual e das imagens da gui
            Engine.getInstance().getCurrentRoomTiles().remove(item);
            ImageMatrixGUI.getInstance().removeImage(item);
        }
    }
}
