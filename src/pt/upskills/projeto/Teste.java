package pt.upskills.projeto;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.*;
import pt.upskills.projeto.rogue.utils.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class Teste {

    public static List<ImageTile> tiles;

    public static void main(String[] args) {
        tiles = new ArrayList<>();
        tiles.add(new Sword(new Position(1, 9)));
        tiles.add(new Hero(new Position(4, 9)));
        tiles.add(new Wall(new Position(7, 10)));

        listarObjetos();
        System.out.println("------");

        saveToFile("saves/tiles.dat");

        System.out.println("A limpar lista");
        tiles.clear();
        listarObjetos();
        System.out.println("------");

        System.out.println("A carregar lista do ficheiro");
        loadFromFile("saves/tiles.dat");
        listarObjetos();

        tiles.add(new Skeleton(new Position(7, 10)));
        System.out.println("----- coisas que mexem:");
        ondeEstaQuemAndar(tiles);
    }

    private static void loadFromFile(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tiles = (List<ImageTile>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveToFile(String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tiles);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listarObjetos() {
        tiles.forEach(tile ->
                System.out.println(tile.getName() + " -> " + tile.getPosition()));
    }

    private static void ondeEstaQuemAndar(List<ImageTile> tiles) {
        tiles.forEach(tile -> {
            if(tile instanceof Movable) {
                System.out.println(tile.getName() + " -> " + tile.getPosition());
            }
        });
    }
}
