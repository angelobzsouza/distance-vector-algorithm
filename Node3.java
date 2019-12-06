import java.util.*;
import java.io.*;
import java.net.*;

public class Node3 {
  public static void main(String[] args) {
    rinit();
  }

  private static void rinit() {
    System.out.println("Iniciando no 3");
    initDistanceTable();
    new Thread(process).start();
    new Thread(listener).start();
  }

  private static void initDistanceTable() {
    Integer i;
    Integer j;
    for (i = 0; i < 4; i++) {
      for (j = 0; j < 4; j++) {
        distanceTable[i][j] = 999;
      }
    }
    distanceTable[0][3] = 7;
    distanceTable[1][3] = 999;
    distanceTable[2][3] = 2;
    distanceTable[3][3] = 0;
  }

  private static Runnable process = new Runnable() {
    public void run() {
      while (true) {
        showMenu();
      }
    }
  };

  private static Runnable listener = new Runnable() {
    public void run() {
      try {
        ServerSocket serverSocket = new ServerSocket(9003);	
        while (true) {
          listen(serverSocket);
        }
      } catch (Exception e) {
        p("Error to start server");
        e.printStackTrace();
      }
    }
  };

  private static void showMenu() {
    Integer opc = -1;
    Integer dest = -1;
    p("Escolha uma oopcao");
    p("1 - Mostrar tabela vetor de distancia");
    p("2 - Enviar pacote de rotina");
    p("3 - Encerrar servico");

    do {
      opc = new Scanner (System.in).nextInt();

      switch (opc) {
        case 1:
          showTable();
        break;
        case 2:
          do {
            p("Digite o numero do no para o qual deseja enviar: ");
            dest = new Scanner (System.in).nextInt();
            if (dest != 0 && dest != 2) {
              p("Destino invalido, tente novamente!");
            }
          } while (dest != 0 && dest != 2);

          toLayer2(dest);
        break;
        case 3:
          System.exit(0);
        break;
        default:
          p("Opcao invalida, tente novamente!");
        break;
      }
    } while (opc != 1 && opc != 2);
  }

  private static void toLayer2 (Integer dest) {
    try {
      Socket socket = new Socket("127.0.0.1", 9000 + dest);
      
      ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
      Package p = new Package(3, dest, distanceTable);
      outputStream.writeObject(p);
      
      socket.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void listen (ServerSocket serverSocket) {
    try {
      Socket socket = serverSocket.accept();

      ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
      Package p = (Package) inputStream.readObject();

      rupdate(p);
      socket.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void rupdate (Package p) {
    Boolean updated = false;
    Integer i;

    for (i = 0; i < 4; i++) {
      distanceTable[i][p.src] = p.distanceTable[i][p.src];
      if (distanceTable[i][3] > p.distanceTable[i][p.src] + distanceTable[p.src][3]) {
        distanceTable[i][3] = p.distanceTable[i][p.src] + distanceTable[p.src][3];
        updated = true;
        System.out.println("mudando: "+updated);

      }
    }
        System.out.println("antes de verificar: "+updated);


    p("Recebeu mensagem de "+p.src);
    showTable();

    if (updated) {
      toLayer2(0);
      toLayer2(2);
    }
  }

  private static void showTable () {
    Integer i;
    Integer j;

    for (i = 0; i < 4; i++) {
      for (j = 0; j < 4; j++) {
        p("A distancia de "+j+" ate "+i+" e: "+distanceTable[i][j]);
      } 
    }

    p("\n");
    p("Tabela formatada:");
    for (i = 0; i < 4; i++) {
      System.out.print(distanceTable[i][0]);
      System.out.print(" ");
      System.out.print(distanceTable[i][1]);
      System.out.print(" ");
      System.out.print(distanceTable[i][2]);
      System.out.print(" ");
      System.out.println(distanceTable[i][3]);
    }
  }

  private static void p (String s) {
    System.out.println(s);
  }

  private static Integer distanceTable[][] = new Integer[4][4];
}