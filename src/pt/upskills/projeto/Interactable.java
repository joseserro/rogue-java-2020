package pt.upskills.projeto;

/**
 * Interface Interactable criada para Itens ou outros objetos do jogo que sejam interagíveis
 * ou seja, quando o Hero tentar ir contra objetos que implementem, deverá chamar o interact
 * esta por sua vez vai devolver algo ao Hero pelo parmetro
 */
public interface Interactable<T> {
    //é enviado através do genérico T o Hero para quando o item for interagido, poder
    //devolver alguma coisa ao hero, ou chamar um método
    public void interact(T obj);
}
