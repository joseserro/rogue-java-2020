package pt.upskills.projeto.objects;

import pt.upskills.projeto.Interactable;
import pt.upskills.projeto.rogue.utils.Position;

/**
 * Classe Item extendida pelos itens do jogo (Sword, Hammer, Key, Meat)
 * Vai implementar a interface Interactible, o que significa que quem extende
 * vai ter de implementar o método interact.
 * Neste caso, passamos o Hero como genérico para o Interactable, ver comentários na classe
 */
public abstract class Item extends GameObject implements Interactable<Hero> {
    public Item(Position position) {
        super(position);
    }
}
