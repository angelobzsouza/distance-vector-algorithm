/*
Angelo Bezerra de Souza - 726496
Bernardo Pinheiro Camargo - 620343
Gabriel Peres de Andrade - 726517
Giuliano Crespe - 743543
*/
import java.io.Serializable;

public class Package implements Serializable {
  public Package (Integer src, Integer dest, Integer[][] distanceTable) {
    this.src = src;
    this.dest = dest;
    this.distanceTable = distanceTable;
  }

  public Integer src;
  public Integer dest;
  public Integer distanceTable[][] = new Integer[4][4];
}