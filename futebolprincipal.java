import java.util.*;

public class futebolprincipal {
  public static void main(String[] args) {
    Scanner entrada = new Scanner(System.in);
    Scanner entradaclube = new Scanner(System.in);
    Scanner entradaPesquisa = new Scanner(System.in);
    Scanner entradaDelete = new Scanner(System.in);
    Scanner vericarUDelete = new Scanner(System.in);
    arquivocrud arqcru = new arquivocrud();
    fut futebas = new fut();

    byte menu = 0;
    boolean contador = false;

    while (contador == false) {
      System.out
          .println(
              "0 - Encerrar Programa \n1 - Cadastrar um Clube \n2 - Realizar partida\n3 - Ler um ID do arquivo\n4 - Realizar Atualização de um Registro\n5 - Realizar o Delete de um Registro ");

      menu = entrada.nextByte();

      switch (menu) {
        case 0:
          contador = true;
          System.out.println("Encerrando o programa...");
          break;
        case 1:
          arqcru.criarClube(entradaclube, futebas);
          break;
        case 2:
          System.out.println("CASE 2 - Realizar Partida");
          break;
        case 3:
          System.out.println("Digite o ID ou Nome do Clube a ser procurado no arquivo");
          String entradaPesquisadeClube = entradaPesquisa.nextLine();
          futebas.printarClubesExistentes(arqcru.procurarClube(entradaPesquisadeClube, futebas));
          break;
        case 4:
          System.out.println("UPDATE");

        case 5:
          System.out.println("Digite o ID para ser deletado");
          String idDelete = vericarUDelete.nextLine();
          arqcru.arquivoDelete(idDelete, vericarUDelete, futebas);
          break;

        case 9:
          arqcru.deletaTudo();
          break;

        default:
      }

    }
    entrada.close();
    entradaDelete.close();
    entradaPesquisa.close();
    entradaclube.close();
    vericarUDelete.close();

  }
}
